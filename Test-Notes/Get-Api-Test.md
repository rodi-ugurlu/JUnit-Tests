# Get API Test Yazımı

Bu yazıda Get API'sinin testini yazmayı inceleyeceğiz.

Yine GIVEN-WHEN-THEN standardını uygulayacağız.

GIVEN'de hazırlık yapıyorduk, bunu zaten önceki notlarda belirtmiştik.

GIVEN'i yazmak için neyin hazırlığını yaptığımızı bilmemiz gerekir. Burada testini yazacağımız metodu bütünüyle anlamalıyız.

Bu bir get API testi olduğu için bize veri döndürmesini bekleriz.

```java 
// GIVEN
List<Todo> todoList = Arrays.asList(testTodo, testTodo2);
when(todoRepository.findAll()).thenReturn(todoList);
```

Burada boş bir liste oluşturup içine daha önce `@BeforeEach` ile işaretlenmiş `setUp()` metodunda oluşturduğumuz 2 adet nesneyi koyuyoruz.

Daha sonra ise WHEN'i uygulayıp testini yazacağımız metodu çağırıp result değişkenine atıyoruz:

```java
// WHEN
List<Todo> result = todoService.findAll();
```

Son olarak THEN'i uygulayıp sonuçları kontrol ediyoruz:

```java
// THEN
assertNotNull(result);
assertEquals(2, result.size());
verify(todoRepository, times(1)).findAll();
```