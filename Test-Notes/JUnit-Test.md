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



3) TEST YAZMAK — adım adım: kod nasıl incelenir, hangi aşamalar, neden bu testleri seçtik? (çok detaylı)

Method seviyesinde okumak (ilk adım)

Metodun imzasına bak: User register(UserRegisterRequest req) → input/return türü belli.

Metodun içinde hangi dış bağımlılıklar var? (repo, passwordEncoder, publisher) — bunlar mock olmalı unit test'te.

Hangi side-effect'ler var? (DB'ye kayıt, event publish) — bunları verify edeceğiz.

Hangi koşullar/branch'ler var? if email exists -> throw (hata yolu) ve diğer yol (başarılı kayıt). Her branch test edilmeli.

Davranışları (behavior) çıkarmak
Sırayla:

Eğer e-mail zaten var ise: EmailAlreadyExistsException fırlatılmalı. (negatif test)

Eğer yoksa: password encode edilecek, user oluşturulup kaydedilecek, event publish edilecek. (pozitif test)

Ek davranış: DB id atamasıyla birlikte dönen user’da id olmalı. (testte simüle edilir)

Çıkarımlar: en az 2 test — 1 hata yolu, 1 happy path. Happy path içinde alt durumları kontrol ederiz (encoded password, role, active, event).

Test matrix (kısa)

Case A: Email exists -> throw, hiçbir şey çağrılmaz (encode/save/publish).

Case B: New email -> encode called, save called with encoded password, event published, returned user has id.
(İstersen ileride: repo.save hata fırlatırsa nasıl davranılır — bu da test edilebilir.)

Hangi bağımlılıkları mock'lamalıyız?

DB repo: mock — unit test hızlı ve deterministik olsun.

Password encoder: mock — böylece encode davranışını kontrol edebilirsin.

Publisher: mock — event publish kontrolü yapacağız.

Not: Integration test'te PasswordEncoder'ın gerçek implementasyonunu ve H2 DB'yi kullanırsın.

Test yazım aşamaları (kodlama aşaması)

AAA (Arrange-Act-Assert) patternini uygula: test içinde üç bölümü net ayır.

Arrange: mock'ların davranışını when(...) ile ayarla. (ör. repo.findByEmail -> Optional.empty())

Act: service.register(req) çağır.

Assert: assertThrows veya assertEquals + verify() + ArgumentCaptor.

Ayrıca ordering test gerekliyse InOrder kullan.

Test isimlendirme

methodName_condition_expectedOutcome → register_whenEmailExists_throwsEmailAlreadyExistsException

Okunması kolay test isimleri, bakınca testin ne yaptığını hemen anlarsın.

Ek doğrulamalar

verifyNoMoreInteractions(...) kullanarak gereksiz çağrı olmadığını da test edebilirsin.

verify(..., times(n)) ile çağrı sayısını denetle.

Hata durumlarını reproducing / debugging

Test başarısız olursa: stack trace’e bak, hangi assert fail oldu gör.

ArgumentCaptor ile mock'a gönderilen nesneyi incele, beklenen mi değil mi anlamak için.

IntelliJ’de test üzerine breakpoint koy, debug ile gir.

System.out.println veya logger geçici kullanmak hızlı bakış için işe yarar (fakat production testlerde temizle).

Neden unit test bu şekilde yazıldı?

Her test tek bir davranışı doğruluyor (single responsibility).

Dış bağımlılıklar mock'landığı için testler deterministik ve hızlı.

InOrder ve ArgumentCaptor ile iş mantığının detaylarını doğrulayabiliyoruz.