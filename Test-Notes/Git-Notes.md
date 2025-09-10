# Git ile Takım Çalışması

Bu yazımızda git konusunu ele alacağız basit bir konu gibi görünse de çoğu yeni başlayan git hakkında developer yüzeysel bilgiye sahiptir.

Öncelikle branch mantığından bahsetmek istiyorum geliştirilmekte olan bir proje olduğunu düşünelim mikro servis mimarisi uygulansın her developer farklı bir servis geliştirdiğini düşünelim herkes farklı iş yapıyor ancak herkes aynı çalışma ortamında yaparsa eğer bu kaosa yol açacaktır çünkü herkes kodu aynı anda pushlamaya çalışırsa eğer conflict dediğimiz çakışma durumu meydana gelir.

Bu yüzden herkes kendi çalışma ortamını ayırır bunu bir evde herkesin kişisel odası var ancak gün sonunda herkes aynı evin işlerini yapıyor diye düşünebiliriz.

İşte branch kavramı burdaya devreye girer türkçesi dal anlamına gelmektedir herkes farklı bir branchte -az önce bahsettiğimiz farklı oda kavramı- çalışır.

- `git branch` komutu ile var olan tüm branchleri listeleyebiliriz
- `git checkout -b <yeni-branch-adi>` bu komut ile aynı anda yeni bir branch oluşturup bu branche geçiş yaparız
- `git checkout <branch-adi>` bu komut ile sadece branch değiştiririz

## Pull Request Süreci

Peki branchler oluşturduk herkes kendi odasında çalışıyor ama günün sonunda bu odaların işlerinin ana evde birleşmesi gerekiyor değil mi işte burada Pull Request kavramı devreye girer.

Pull Request kısaca PR türkçe çekme isteği anlamına gelir ne demek bu sen kendi branchinde işini bitirdin ve arkadaşlar benim yaptığım işi ana projeye ekleyelim diyorsun tıpkı evde kendi odanda yaptığın temizliği ana salonla birleştirmeni istemen gibi.

Diyelim ki takımda 5 kişi varsın:
- Ali: `feature/login-system`
- Ayşe: `feature/user-profile`
- Mehmet: `feature/payment-system`
- Fatma: `feature/admin-panel`
- Sen: `feature/product-catalog`

Herkes kendi branchinde çalışıyor ve işi bittiğinde PR açıyor ama dikkat et burada 5 kişi aynı anda PR açarsa bu PRlar tek tek incelenir ve merge edilir aynı anda hepsi birleştirilmez çünkü bu kaosa yol açar.

İlk olarak Ali nin PRı incelenir ve main branche merge edilir şimdi main branchde Alinin kodları var ama Ayşe nin kodu hala eski maine göre yazılmış bu durumda Ayşe şunları yapmalı:

```bash
git checkout main
git pull origin main  # güncel maini al alinın kodlarıyla birlikte
git checkout feature/user-profile  # kendi branchine geri dön
git merge main  # ana branchdeki güncellemeleri kendi branchine al
```

Eğer bu işlem sırasında conflict çıkarsa yani alinin değiştirdiği dosyaları ayşe de değiştirmişse git bize kardeşim burada bir çakışma var sen karar ver der bu durumda conflicted dosyaları manuel olarak düzenleyip sorunu çözeriz.

## GitHub/GitLab da PR Oluşturma

GitHub da PR oluşturma süreci şöyle:

1. Kendi branchini push et: `git push origin feature/benim-ozelligim`
2. GitHub web sitesine git
3. Sarı bildirim çıkacak "feature/benim-ozelligim had recent pushes"
4. "Compare & pull request" butonuna tıkla
5. PR formunu doldur başlık açıklama yaz ne yaptığını anlat
6. "Create pull request" bas artık takım arkadaşların görebilir

## Code Review Süreci

PR açıldığında takım arkadaşların kodunu inceler yorum yapar öneri verir bu fonksiyon çok karmaşık bölebilir misin veya bu değişken ismi daha açıklayıcı olabilir gibi bu süreç kod kalitesini artırır ve herkesin birbirinden öğrenmesini sağlar.

İnceleme sonunda:
- ✅ "Approve" onaylıyorum
- ❌ "Request changes" değişiklik istiyorum

## Merge İşlemi

PR onaylandığında merge edilir burada farklı seçenekler var:

**Merge Commit:** Tüm commit geçmişi korunur
```
main branch:
A---B---C---D
         \   \
          E---F---G (merge commit)
```

**Squash and Merge:** Tüm commitler tek commite dönüştürülür daha temiz geçmiş
```
main branch:
A---B---C---D---G (tek commit olarak)
```

## Günlük Çalışma Rutini

Her gün işe başlarken:
```bash
git checkout main
git pull origin main
git checkout feature/benim-ozelligim
git merge main  # ana daldaki güncellemeleri al
```

İş bittiğinde:
```bash
git add .
git commit -m "açıklayıcı mesaj"
git push origin feature/benim-ozelligim
```

Bu sistem sayesinde 5 10 hatta 100 developer bile birbirinin ayağına basmadan düzenli ve kontrollü şekilde aynı proje üzerinde çalışabilir en önemlisi asla main branche direkt push etme her özellik için ayrı branch aç günlük mainден update al açıklayıcı commit mesajları yaz ve PR lerde code review yap.

## Önemli Komutlar

```bash
# hangi branchtesin
git branch

# dosya durumunu gör
git status

# commit geçmişi
git log --oneline

# remote branchleri gör
git branch -r

# branch sil local
git branch -d feature/benim-ozelligim

# remotetan branch sil
git push origin --delete feature/benim-ozelligim
```