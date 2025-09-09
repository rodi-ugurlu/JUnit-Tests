# Put API Test Yazımı

Put API'leri için test yazarken aslında daha önce yazdığımız testler ile aynı yolu izleyeceğiz. Tekrar tekrar anlatmaya gerek duymuyorum.

Update işlemlerinde genel olarak id üzerinden işlem yapılır. Verilen id ile veritabanında ilgili nesnenin durumu sorgulanır, mevcut ise gerekli işlemler yapılır.

Bu aşamalar GIVEN standardına giriyor:

```java
// GIVEN
when(todoRepository.findById(1)).thenReturn(Optional.of(testTodo));
when(todoRepository.save(any(Todo.class))).thenReturn(testTodo);
```

Bu senaryoda bir todo'nun statusünü done yapacağız.

WHEN standardı:

```java
Todo result = todoService.markDone(1);
```

Son olarak THEN:

```java
// THEN
assertTrue(result.isDone()); // Done true olmalı
verify(todoRepository, times(1)).findById(1); // findById çağrıldı mı?
verify(todoRepository, times(1)).save(testTodo); // save çağrıldı mı?
```

Son olarak biz başarılı olan senaryoyu yazdık. Başarısız senaryoyu da ele almalıydık, bunu unutmamalıyız.