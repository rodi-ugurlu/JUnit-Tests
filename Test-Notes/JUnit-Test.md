JUnit 5 Nedir?
Java'da birim testleri (unit tests) yazmak için kullanılan framework

Metotların beklenen şekilde çalışıp çalışmadığını kontrol eder

Mockito Nedir?
Testlerde "taklit nesneler" (mock objects) oluşturmak için kullanılır

Gerçek database'e bağlanmak yerine taklit repository'ler kullanırız

@Test // → Bu bir test metodu olduğunu belirtir
@BeforeEach // → Her testten ÖNCE çalışacak metod
@AfterEach // → Her testten SONRA çalışacak metod
@BeforeAll // → Tüm testlerden ÖNCE bir kere çalışacak (static metod)
@AfterAll // → Tüm testlerden SONRA bir kere çalışacak (static metod)
@DisplayName("Açıklayıcı İsim") // → Teste okunabilir isim verir
@Mock // → Taklit nesne oluşturur
@InjectMocks // → Mock'ları gerçek sınıfa enjekte eder
@ExtendWith(MockitoExtension.class) // → Mockito'yu JUnit 5 ile entegre eder

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

// Koleksiyon kontrolleri
assertThat(list).hasSize(3);
assertThat(list).contains("item1", "item2");