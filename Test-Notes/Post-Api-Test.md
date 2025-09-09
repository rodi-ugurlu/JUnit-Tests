# Unit Test Nedir ve Nasıl Yazılır

Unit test, bir metod veya class için yazılan test türüdür.

Test yazmaya başlamak için testi yazılacak class içinde generate butonuna basılır ve test seçilir. Ardından testi yazmak istenen metodlar seçilip ilgili test class oluşturulur.

Bu notta service testi yazmayı inceleyeceğiz. Öncelikle ilgili class içinde kullanılan başka dependency classlar var ise bunlar `@Mock` anotasyonu ile taklidini oluşturup `@InjectMocks` anotasyonu ile class içine inject edilir.

`@Test` anotasyonu ile işaretlenen fonksiyonun isimlendirme standardı genelde `metodAdı_dönmesiBeklenenDeğer()` şeklindedir. Örneğin: `create_ShouldReturnSavedTodo()`

Test yazılırken genelde bu standarda bağlı kalırız. Bunlar GIVEN-WHEN-THEN'dir:

- **GIVEN**: İngilizcesi "diyelim ki" anlamına gelen bu standartta test için gerekli veriler hazırlanır
- **WHEN**: İngilizcesi "yaptığımızda" anlamına gelen bu standartta test edeceğimiz metodu çağırırız
- **THEN**: İngilizcesi "olması beklenir" anlamına gelen bu standartta sonuçları ve mockların doğru çalışıp çalışmadığını kontrol ederiz

Şimdi bu standartları 4 temel API için uygulayalım: Get, Post, Put, Delete.

Öncelikle Create yani post için nasıl test yazıldığını inceleyelim. Test classının başında `@BeforeEach` anotasyonu ile işaretlenmiş bir setUp fonksiyonu ile ilgili classın kullandığı entitylerden birkaç adet initialize edilir.

Bunları setUp fonksiyonunu kullanmadan her test metodunun içinde ayrı ayrı tanımlayabiliriz fakat bu yöntem kod tekrarına yol açar, best practice değildir.

Entity'mizin Todo adında olduğunu varsayarak örneğe devam ediyorum:

```java
@BeforeEach
void setUp() {
    testTodo = new Todo(1, "Alışveriş yap", false);
    testTodo2 = new Todo(2, "Kitap oku", true);
}
```

2 adet nesne initialize ettikten sonra artık test yazmaya başlayabiliriz. Test metodlarında bunlara ihtiyacımız olacak.

```java
@Test
@DisplayName("Yeni todo başarıyla oluşturulmalı")
void create_ShouldReturnSavedTodo() {
    // GIVEN
    when(todoRepository.save(any(Todo.class))).thenReturn(testTodo);
    // WHEN
    Todo result = todoService.create(testTodo);
    // THEN
    assertNotNull(result);
    assertEquals(testTodo.getId(), result.getId());
    assertEquals("Alışveriş yap", result.getTitle());
    verify(todoRepository, times(1)).save(any(Todo.class));
}
```

Metod yazımına GIVEN'i uygulayarak başlıyoruz. Burada hazırlık yapıyoruz. Temel mantığı test başlamadan testin döneceği değer zaten belli olduğu için burada onu verdik:

```java
when(todoRepository.save(any(Todo.class))).thenReturn(testTodo);
```

Daha sonra ise WHEN'i uygulayarak metodu çağırıp result değişkenine atıyoruz:

```java
Todo result = todoService.create(testTodo);
```

En son aşama olan THEN'i uygulayarak testin sonuçlarını kontrol ediyoruz:

```java
assertNotNull(result);
assertEquals(testTodo.getId(), result.getId());
assertEquals("Alışveriş yap", result.getTitle());
verify(todoRepository, times(1)).save(any(Todo.class));
```