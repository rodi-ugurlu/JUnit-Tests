# JUnit 5 ve Mockito ile Test Yazma Rehberi

## JUnit 5 Nedir?

JUnit 5, Java'da birim testleri (unit tests) yazmak için kullanılan framework'tür. Metodların beklenen şekilde çalışıp çalışmadığını kontrol eder ve kodumuzun güvenilirliğini artırır.

## Mockito Nedir?

Mockito, testlerde "taklit nesneler" (mock objects) oluşturmak için kullanılır. Gerçek database'e bağlanmak yerine taklit repository'ler kullanarak testlerimizi hızlı ve deterministik hale getirir.

## Test Anotasyonları (En Çok Kullanılanlar)

### Temel Test Anotasyonları

- `@Test` → Bu bir test metodu olduğunu belirtir
- `@DisplayName("Açıklayıcı İsim")` → Teste okunabilir isim verir
- `@Disabled` → Test'i geçici olarak devre dışı bırakır
- `@RepeatedTest(5)` → Testi 5 kere tekrarlar
- `@ParameterizedTest` → Farklı parametrelerle aynı testi çalıştırır

### Lifecycle Anotasyonları

- `@BeforeEach` → Her testten ÖNCE çalışacak metod
- `@AfterEach` → Her testten SONRA çalışacak metod
- `@BeforeAll` → Tüm testlerden ÖNCE bir kere çalışacak (static metod)
- `@AfterAll` → Tüm testlerden SONRA bir kere çalışacak (static metod)

### Mockito Anotasyonları

- `@Mock` → Taklit nesne oluşturur
- `@InjectMocks` → Mock'ları gerçek sınıfa enjekte eder
- `@ExtendWith(MockitoExtension.class)` → Mockito'yu JUnit 5 ile entegre eder
- `@Spy` → Gerçek nesnenin bir kısmını taklit eder
- `@Captor` → ArgumentCaptor oluşturur

### Conditional Test Anotasyonları

- `@EnabledOnOs(OS.LINUX)` → Sadece Linux'ta çalışır
- `@DisabledOnOs(OS.WINDOWS)` → Windows'ta çalışmaz
- `@EnabledOnJre(JRE.JAVA_11)` → Sadece Java 11'de çalışır
- `@EnabledIf("customCondition")` → Özel koşul varsa çalışır

## Assert Metodları

```java
// Eşitlik kontrolü
assertEquals(expected, actual);
assertNotEquals(expected, actual);

// Null kontrolleri
assertNull(value);
assertNotNull(value);

// Boolean kontrolleri
assertTrue(condition);
assertFalse(condition);

// Exception kontrolleri
assertThrows(IllegalArgumentException.class, () -> {
    service.methodThatThrowsException();
});

// Koleksiyon kontrolleri (AssertJ ile)
assertThat(list).hasSize(3);
assertThat(list).contains("item1", "item2");
assertThat(list).isEmpty();
assertThat(string).startsWith("Hello");
```

## Test Yazma Süreci: Adım Adım

### 1. Metod Seviyesinde İnceleme (İlk Adım)

Test yazacağın metodun imzasını incele. Örneğin: `User register(UserRegisterRequest req)`

- **Input/Return türü**: Ne alıyor, ne döndürüyor?
- **Dış bağımlılıklar**: Hangi servisleri kullanıyor? (repo, passwordEncoder, publisher)
- **Side-effect'ler**: DB'ye kayıt yapıyor mu? Event publish ediyor mu?
- **Koşullar/Branch'ler**: Hangi if/else durumları var?

### 2. Davranışları (Behavior) Çıkarmak

Her farklı durum için ayrı test yazılmalı:

**Örnek Register Metodu için:**
- Eğer email zaten var ise: `EmailAlreadyExistsException` fırlatılmalı (negatif test)
- Eğer yoksa: password encode edilecek, user kaydedilecek, event publish edilecek (pozitif test)

### 3. Test Matrix Oluşturmak

**Case A**: Email exists → Exception throw, hiçbir şey çağrılmaz
**Case B**: New email → encode called, save called, event published

### 4. Mock'ları Belirlemek

Unit test'te dış bağımlılıkları mock'lamalıyız:
- **DB Repository**: Mock (hızlı ve deterministik test için)
- **Password Encoder**: Mock (encode davranışını kontrol için)
- **Event Publisher**: Mock (event publish kontrolü için)

### 5. Test Yazım Aşamaları (AAA Pattern)

**AAA (Arrange-Act-Assert)** patternini uygula:

```java
@Test
@DisplayName("Email zaten var ise exception fırlatılmalı")
void register_whenEmailExists_throwsEmailAlreadyExistsException() {
    // ARRANGE (GIVEN)
    UserRegisterRequest request = new UserRegisterRequest("test@test.com", "password");
    when(userRepository.findByEmail("test@test.com"))
        .thenReturn(Optional.of(existingUser));
    
    // ACT & ASSERT (WHEN & THEN)
    assertThrows(EmailAlreadyExistsException.class, () -> {
        userService.register(request);
    });
    
    // THEN - Verify hiçbir side-effect olmadığını kontrol et
    verify(passwordEncoder, never()).encode(anyString());
    verify(userRepository, never()).save(any(User.class));
    verify(eventPublisher, never()).publishEvent(any());
}
```

### 6. Test İsimlendirme Standardı

Format: `metodAdı_koşul_beklenenSonuç`

Örnekler:
- `register_whenEmailExists_throwsEmailAlreadyExistsException`
- `register_whenNewEmail_returnsUserWithId`
- `findById_whenUserExists_returnsUser`
- `delete_whenUserNotFound_throwsNotFoundException`

## Happy Path Test Örneği

```java
@Test
@DisplayName("Yeni email ile kayıt başarılı olmalı")
void register_whenNewEmail_returnsUserWithId() {
    // ARRANGE
    UserRegisterRequest request = new UserRegisterRequest("new@test.com", "password");
    String encodedPassword = "encodedPassword";
    User savedUser = new User(1L, "new@test.com", encodedPassword, Role.USER, true);
    
    when(userRepository.findByEmail("new@test.com")).thenReturn(Optional.empty());
    when(passwordEncoder.encode("password")).thenReturn(encodedPassword);
    when(userRepository.save(any(User.class))).thenReturn(savedUser);
    
    // ACT
    User result = userService.register(request);
    
    // ASSERT
    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("new@test.com", result.getEmail());
    assertEquals(encodedPassword, result.getPassword());
    assertTrue(result.isActive());
    assertEquals(Role.USER, result.getRole());
    
    // Verify interactions
    verify(passwordEncoder, times(1)).encode("password");
    verify(userRepository, times(1)).save(any(User.class));
    verify(eventPublisher, times(1)).publishEvent(any(UserRegisteredEvent.class));
}
```

## ArgumentCaptor Kullanımı

Bazen mock'a gönderilen nesneyi detaylı incelemek isteriz:

```java
@Test
void register_whenNewEmail_savesCorrectUser() {
    // ARRANGE
    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    // ... other setup
    
    // ACT
    userService.register(request);
    
    // ASSERT
    verify(userRepository).save(userCaptor.capture());
    User capturedUser = userCaptor.getValue();
    
    assertEquals("new@test.com", capturedUser.getEmail());
    assertEquals("encodedPassword", capturedUser.getPassword());
    assertEquals(Role.USER, capturedUser.getRole());
}
```

## InOrder ile Sıralı Çağrı Kontrolü

```java
@Test
void register_callsMethodsInCorrectOrder() {
    // ARRANGE & ACT
    userService.register(request);
    
    // ASSERT - Metodların doğru sırada çağrıldığını kontrol et
    InOrder inOrder = inOrder(userRepository, passwordEncoder, eventPublisher);
    inOrder.verify(userRepository).findByEmail("new@test.com");
    inOrder.verify(passwordEncoder).encode("password");
    inOrder.verify(userRepository).save(any(User.class));
    inOrder.verify(eventPublisher).publishEvent(any(UserRegisteredEvent.class));
}
```

## Test Class Yapısı Örneği

```java
@ExtendWith(MockitoExtension.class)
@DisplayName("User Service Tests")
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock 
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private ApplicationEventPublisher eventPublisher;
    
    @InjectMocks
    private UserService userService;
    
    private User existingUser;
    private UserRegisterRequest validRequest;
    
    @BeforeEach
    void setUp() {
        existingUser = new User(1L, "existing@test.com", "password", Role.USER, true);
        validRequest = new UserRegisterRequest("new@test.com", "password123");
    }
    
    @Test
    @DisplayName("Email zaten var ise exception fırlatılmalı")
    void register_whenEmailExists_throwsException() {
        // Test implementation...
    }
    
    // Diğer testler...
}
```

## Debug ve Troubleshooting

### Test Başarısız Olduğunda

1. **Stack trace'i incele**: Hangi assert fail oldu?
2. **ArgumentCaptor kullan**: Mock'a gönderilen değerleri kontrol et
3. **Breakpoint koy**: IntelliJ'de test üzerine debug koy
4. **Geçici log ekle**: `System.out.println` veya logger kullan (sonra temizle)

### Yaygın Hatalar

- Mock'ların davranışı tanımlanmamış (`when().thenReturn()` eksik)
- Verify'da yanlış parametre (`any()` vs gerçek değer)
- Test'te gerçek implementasyon çağrılıyor (mock eksik)
- Exception test'inde `assertThrows` kullanılmamış

## Neden Bu Şekilde Test Yazıyoruz?

1. **Tek sorumluluk**: Her test tek bir davranışı doğruluyor
2. **Hızlı ve deterministik**: Dış bağımlılıklar mock'landığı için
3. **Detaylı kontrol**: InOrder ve ArgumentCaptor ile iş mantığının detaylarını doğrulayabiliyoruz
4. **Okunabilir**: Test isimleri ve yapı sayesinde ne test edildiği anlaşılıyor

Bu rehberi takip ederek güvenilir, hızlı ve bakımı kolay unit testler yazabilirsin moruk!