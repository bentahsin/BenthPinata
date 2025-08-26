# BenthPinata ✨
### Gelişmiş ve Özelleştirilebilir Minecraft Piñata Etkinlik Eklentisi

![Java](https://img.shields.io/badge/Java-17-blue.svg) ![Spigot API](https://img.shields.io/badge/API-Spigot%201.13%2B-orange.svg) ![License](https://img.shields.io/badge/License-MIT-green.svg)

*Spigot, Paper, Purpur ve tüm uyumlu sunucu yazılımları için tasarlanmıştır.*

**BenthPinata**, Minecraft sunucunuza tamamen özelleştirilebilir, eğlenceli ve ödüllendirici Piñata etkinlikleri ekleyen kapsamlı bir Spigot eklentisidir. Sunucu sahipleri için tasarlanan bu eklenti ile oyuncularınızın bir araya gelip ortak bir hedefe saldırmasını, rekabet etmesini ve harika ödüller kazanmasını sağlayın. Kolay yapılandırması ve güçlü özellikleriyle sunucunuza yeni bir soluk getirin!

<br>

---

## 🚀 Temel Özellikler

*   **Sınırsız Piñata Türü:** Farklı can, konum ve yeteneklere sahip istediğiniz kadar Piñata türü oluşturun.
*   **Gelişmiş Yetenek Sistemi:** Piñata'nın canı azaldıkça etrafındaki oyuncuları savurma, kör etme veya negatif iksir efektleri uygulama gibi özel yetenekler tetiklensin.
*   **Kapsamlı Ödül Mekanizması:**
    *   **Vuruş Başına:** Oyuncular her vuruşta şansa bağlı küçük ödüller kazanabilir.
    *   **Hasar Eşiği:** Belirli bir hasar miktarına ulaşan oyunculara garantili ödüller verin.
    *   **Final Ödülleri:** Etkinlik bittiğinde en çok hasar vuran oyuncuları büyük ödüllerle onurlandırın.
*   **Görsel Geri Bildirim:**
    *   **DecentHolograms** entegrasyonu ile Piñata'nın üzerinde canını gösteren dinamik hologramlar.
    *   Tüm oyuncular için anlık can takibi sağlayan **Boss Bar**.
    *   Etkileyici **partikül ve ses efektleri**.
*   **Otomatik Etkinlik Sistemi:**
    *   Belirli aralıklarla (örneğin her 2 saatte bir) otomatik etkinlik başlatın.
    *   Sunucudaki oyuncu sayısı belirli bir hedefe ulaştığında etkinliği tetikleyin.
*   **İstatistikler ve Liderlik Tablosu:** Oyuncuların toplam hasarını ve öldürdüğü Piñata sayısını takip edin. `/pinata stats top` komutu ile en iyileri listeleyin.
*   **PlaceholderAPI Desteği:** Eklentinin istatistiklerini ve sıralamalarını diğer eklentilerde (skor tablosu, menüler vb.) kullanın.
*   **Kolay Yönetim:** Basit ve yetkilendirilmiş komutlarla etkinlikleri kolayca yönetin.

---

## ✅ Desteklenen Sürümler

Bu eklenti, modern sunucu API'leri üzerine inşa edilmiştir ve geniş bir sürüm yelpazesiyle uyumluluğu test edilmiştir.

| Minecraft Sürümü | Destek Durumu | Minecraft Sürümü | Destek Durumu |
| :--------------- | :-----------: | :--------------- | :-----------: |
| **1.21.X**       |   ✅ Uyumlu   | **1.16.X**             |   ✅ Uyumlu   |
| **1.20.X**       |   ✅ Uyumlu   | **1.15.X**             |   ✅ Uyumlu   |
| **1.19.X**       |   ✅ Uyumlu   | **1.14.X**             |   ✅ Uyumlu   |
| **1.18.X**       |   ✅ Uyumlu   | **1.13.X**             |   ✅ Uyumlu   |
| **1.17.X**       |   ✅ Uyumlu   | **1.12.X & Daha Eski** |   ❌ Uyumsuz  |

---

## 🔧 Kurulum

1.  **Eklentiyi İndirin:** [Releases](https://github.com/bentahsin/BenthPinata/releases) sayfasından en son `.jar` dosyasını indirin.
2.  **Bağımlılıkları Kurun:**
    *   **Zorunlu:** [DecentHolograms](https://www.spigotmc.org/resources/decentholograms.96927/) eklentisinin sunucunuzda kurulu olduğundan emin olun.
    *   **İsteğe Bağlı:** Daha iyi bir deneyim için [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) eklentisini kurmanız şiddetle tavsiye edilir.
3.  **Dosyaları Yükleyin:** İndirdiğiniz `BenthPinata.jar` dosyasını ve bağımlılık eklentilerini sunucunuzun `plugins` klasörüne atın.
4.  **Sunucuyu Başlatın:** Sunucuyu başlatın (veya yeniden yükleyin). `plugins/BenthPinata` klasörü içinde gerekli yapılandırma dosyaları (`config.yml`, `messages.yml`, vb.) otomatik olarak oluşturulacaktır.
5.  **Yapılandırmayı Düzenleyin:** Piñata'nın doğacağı konumu ve diğer ayarları `config.yml` dosyasından kendinize göre düzenleyin. Değişikliklerin etkili olması için sunucuda `/pinata reload` komutunu kullanın.

---

## 💻 Komutlar ve Yetkiler

| Komut                                | Açıklama                                             | Yetki (Permission)              |
| ------------------------------------ | ---------------------------------------------------- | ------------------------------- |
| `/pinata help`                       | Yardım menüsünü gösterir.                            | `benthpinata.command.help`      |
| `/pinata start <tür>`                | Belirtilen türde bir Piñata etkinliği başlatır.      | `benthpinata.command.start`     |
| `/pinata killall`                    | Aktif olan tüm Piñata'ları ve görevleri sonlandırır. | `benthpinata.command.killall`   |
| `/pinata reload`                     | Eklentinin tüm yapılandırma dosyalarını yeniden yükler. | `benthpinata.command.reload`    |
| `/pinata stats`                      | Kendi Piñata istatistiklerinizi gösterir.            | `benthpinata.stats`             |
| `/pinata stats top <damage\|kills>` | En çok hasar veren veya Piñata öldürenleri listeler. | `benthpinata.stats.top`         |
| `/pinata stats reset <oyuncu\|all>`  | Bir oyuncunun veya herkesin istatistiklerini sıfırlar. | `benthpinata.stats.reset`       |
| **(Tüm Yetkiler)**                   | Yukarıdaki tüm komutlara erişim sağlar.               | `benthpinata.admin`             |

---

## ⚙️ Yapılandırma Dosyaları

Eklenti, tüm ayarlarını `plugins/BenthPinata/` klasöründeki dosyalarda saklar.

*   `config.yml`: Genel ayarlar, Piñata türleri, konumlar, can değerleri, efektler ve otomatik etkinlik sistemi gibi ana yapılandırmaları içerir.
*   `messages.yml`: Eklentideki tüm kullanıcıya dönük mesajları, başlıkları ve hologram metinlerini içerir. Tamamen Türkçeleştirilebilir.
*   `rewards.yml`: Vuruş başına, hasar eşiğine ve etkinlik sonuna özel tüm ödül komutlarını ve şanslarını içerir.
*   `abilities.yml`: Piñata türlerine özel yeteneklerin tanımlandığı dosyadır.
*   `stats.yml`: Oyuncu istatistiklerinin saklandığı veri dosyasıdır. Bu dosyayı manuel olarak düzenlemeniz önerilmez.

---

##  PAPI Desteği

Eğer sunucunuzda `PlaceholderAPI` kuruluysa, aşağıdaki placeholder'ları kullanarak BenthPinata verilerini diğer eklentilerde gösterebilirsiniz.

### Oyuncu İstatistikleri
*   `%benthpinata_stats_damage%` - Oyuncunun toplam Piñata hasarını gösterir.
*   `%benthpinata_stats_kills%` - Oyuncunun toplam Piñata öldürme sayısını gösterir.

### Liderlik Tablosu
*   `%benthpinata_top_damage_<rank>_name%` - Hasar sıralamasındaki `<rank>`. oyuncunun adını gösterir (örn: `%benthpinata_top_damage_1_name%`).
*   `%benthpinata_top_damage_<rank>_value%` - Hasar sıralamasındaki `<rank>`. oyuncunun hasarını gösterir.
*   `%benthpinata_top_kills_<rank>_name%` - Öldürme sıralamasındaki `<rank>`. oyuncunun adını gösterir.
*   `%benthpinata_top_kills_<rank>_value%` - Öldürme sıralamasındaki `<rank>`. oyuncunun öldürme sayısını gösterir.

---

## 💬 Destek ve Geri Bildirim

Bir hata bulursanız veya bir özellik öneriniz varsa, lütfen [GitHub Issues](https://github.com/bentahsin/BenthPinata/issues) sayfası üzerinden bir bildirim oluşturun.

---

## 📄 Lisans

Bu proje [MIT Lisansı](LICENSE) altında lisanslanmıştır.
