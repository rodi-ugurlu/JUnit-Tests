# Delete API Test Açıklaması

Delete API'leri için yazılan testleri özel kılan şey genelde geriye hiçbir değer return etmemesidir.

Normal metod çağrılarında `when()..thenReturn()` tarzı ifadeler kullanıyorduk, hatırlarsanız.

Delete metodları void tipinde olduğu için bunları kullanamayız. Bunun yerine `doNothing()` kullanırız, yani hiçbir değer döndürmeyeceğimizi bildirmiş oluruz.

```java
// GIVEN
when(todoRepository.existsById(1)).thenReturn(true);
doNothing().when(todoRepository).deleteById(1);
```

```java
// WHEN & THEN
assertDoesNotThrow(() -> todoService.delete(1)); // Hata fırlatmamalı

verify(todoRepository, times(1)).existsById(1);
verify(todoRepository, times(1)).deleteById(1);
```