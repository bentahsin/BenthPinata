<div align="center">

# ✨ BenthPinata ✨

### Gelişmiş ve Özelleştirilebilir Minecraft Piñata Etkinlik Eklentisi

![Java](https://img.shields.io/badge/Java-17-blue.svg) ![Spigot API](https://img.shields.io/badge/API-Spigot%201.13%2B-orange.svg) ![License](https://img.shields.io/badge/License-MIT-green.svg) ![GitHub Stars](https://img.shields.io/github/stars/bentahsin/BenthPinata?style=social) ![GitHub Issues](https://img.shields.io/github/issues/bentahsin/BenthPinata)

*Spigot, Paper, Purpur ve tüm uyumlu sunucu yazılımları için tasarlanmıştır.*

</div>

**BenthPinata**, Minecraft sunucunuza tamamen özelleştirilebilir, eğlenceli ve ödüllendirici Piñata etkinlikleri ekleyen kapsamlı bir Spigot eklentisidir. Sunucu sahipleri için tasarlanan bu eklenti ile oyuncularınızın bir araya gelip ortak bir hedefe saldırmasını, rekabet etmesini ve harika ödüller kazanmasını sağlayın. Kolay yapılandırması ve güçlü özellikleriyle sunucunuza yeni bir soluk getirin!

<br>

---

## 📜 İçindekiler
* [Neden BenthPinata?](#-neden-benthpinata)
* [Temel Özellikler](#-temel-özellikler)
* [Desteklenen Sürümler](#-desteklenen-sürümler)
* [Kurulum](#-kurulum)
* [Komutlar ve Yetkiler](#-komutlar-ve-yetkiler)
* [Yapılandırma Dosyaları](#-yapılandırma-dosyaları)
* [Örnek Yapılandırma](#-örnek-yapılandırma)
* [PAPI Desteği](#-papi-desteği)
* [Destek ve Geri Bildirim](#-destek-ve-geri-bildirim)
* [Katkıda Bulunma](#-katkıda-bulunma)
* [Lisans](#-lisans)

---

## 🤔 Neden BenthPinata?

* **🏆 Yüksek Performans:** Modern kodlama teknikleri ve asenkron işlemler sayesinde sunucunuzda en düşük etkiyle çalışır.
* **🌍 Tamamen Türkçe:** Tüm mesajlar ve yapılandırma dosyaları, Türk oyuncu topluluğu için özenle hazırlanmıştır.
* **🔧 Sınırsız Esneklik:** Hayalinizdeki etkinliği yaratmak için hemen hemen her ayarı (`config.yml`, `rewards.yml`, `abilities.yml`) değiştirebilirsiniz.
* **✅ Aktif Destek:** Sürekli güncellenen ve topluluk geri bildirimlerine önem veren bir proje.

---

## 🚀 Temel Özellikler

* **Sınırsız Piñata Türü:** Farklı can, konum ve yeteneklere sahip istediğiniz kadar Piñata türü oluşturun.
    * `Gelişmiş Mob Desteği:` Piñata'nızı sadece bir koyun değil, bir **Zombi, At, Lama** ve hatta **NBT etiketleri** ile tamamen özelleştirilmiş herhangi bir yaratık yapın!
* **Gelişmiş Yetenek Sistemi:** Piñata'nın canı azaldıkça etrafındaki oyuncuları savurma, kör etme veya negatif iksir efektleri uygulama gibi özel yetenekler tetiklensin.
* **Kapsamlı Ödül Mekanizması:**
    * **Vuruş Başına:** Oyuncular her vuruşta şansa bağlı küçük ödüller kazanabilir.
    * **Hasar Eşiği:** Belirli bir hasar miktarına ulaşan oyunculara garantili ödüller verin.
    * **Final Ödülleri:** Etkinlik bittiğinde en çok hasar vuran oyuncuları büyük ödüllerle onurlandırın.
* **Görsel Geri Bildirim:**
    * **DecentHolograms** entegrasyonu ile Piñata'nın üzerinde canını gösteren dinamik hologramlar.
    * Tüm oyuncular için anlık can takibi sağlayan **Boss Bar**.
    * Etkileyici **partikül ve ses efektleri**.
* **Otomatik Etkinlik Sistemi:**
    * Belirli aralıklarla (örneğin her 2 saatte bir) otomatik etkinlik başlatın.
    * Sunucudaki oyuncu sayısı belirli bir hedefe ulaştığında etkinliği tetikleyin.
    * `Yeni!` Belirli gün ve saatlerde (örn: Her Cuma 20:00) **zamanlanmış etkinlikler** planlayın.
* **İstatistikler ve Liderlik Tablosu:** Oyuncuların toplam hasarını ve öldürdüğü Piñata sayısını takip edin. `/pinata stats top` komutu ile en iyileri listeleyin.
* **PlaceholderAPI Desteği:** Eklentinin istatistiklerini ve sıralamalarını diğer eklentilerde (skor tablosu, menüler vb.) kullanın.
* **Kolay Yönetim:** Basit ve yetkilendirilmiş komutlarla etkinlikleri kolayca yönetin.

---

## ✅ Desteklenen Sürümler

Bu eklenti, modern sunucu API'leri üzerine inşa edilmiştir ve geniş bir sürüm yelpazesiyle uyumluluğu test edilmiştir.

| Minecraft Sürümü | Destek Durumu | Minecraft Sürümü | Destek Durumu |
| :--------------- | :-----------: | :--------------- | :-----------: |
| **1.21.X** |   ✅ Uyumlu   | **1.16.X** |   ✅ Uyumlu   |
| **1.20.X** |   ✅ Uyumlu   | **1.15.X** |   ✅ Uyumlu   |
| **1.19.X** |   ✅ Uyumlu   | **1.14.X** |   ✅ Uyumlu   |
| **1.18.X** |   ✅ Uyumlu   | **1.13.X** |   ✅ Uyumlu   |
| **1.17.X** |   ✅ Uyumlu   | **1.12.X & Daha Eski** |   ❌ Uyumsuz  |

---

## 🔧 Kurulum

1.  **Eklentiyi İndirin:** [Releases](https://github.com/bentahsin/BenthPinata/releases) sayfasından en son `.jar` dosyasını indirin.
2.  **Bağımlılıkları Kurun:**
    * **Zorunlu:** [DecentHolograms](https://www.spigotmc.org/resources/decentholograms.96927/) eklentisinin sunucunuzda kurulu olduğundan emin olun.
    * **İsteğe Bağlı:** Daha iyi bir deneyim için [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) eklentisini kurmanız şiddetle tavsiye edilir.
3.  **Dosyaları Yükleyin:** İndirdiğiniz `BenthPinata.jar` dosyasını ve bağımlılık eklentilerini sunucunuzun `plugins` klasörüne atın.
4.  **Sunucuyu Başlatın:** Sunucuyu başlatın (veya yeniden yükleyin). `plugins/BenthPinata` klasörü içinde gerekli yapılandırma dosyaları (`config.yml`, `messages.yml`, vb.) otomatik olarak oluşturulacaktır.
5.  **Yapılandırmayı Düzenleyin:** Piñata'nın doğacağı konumu ve diğer ayarları `config.yml` dosyasından kendinize göre düzenleyin. Değişikliklerin etkili olması için sunucuda `/pinata reload` komutunu kullanın.

---

## 💻 Komutlar ve Yetkiler

> `Not:` Ana komut olan `/pinata` yerine `/pinataevent` veya `/pinyata` da kullanabilirsiniz.

| Komut                                | Açıklama                                             | Yetki (Permission)              |
| ------------------------------------ | ---------------------------------------------------- | ------------------------------- |
| `/pinata help`                       | Yardım menüsünü gösterir.                            | `benthpinata.command.help`      |
| `/pinata start <tür> [konum]`        | Belirtilen türde bir Piñata etkinliği başlatır.      | `benthpinata.command.start`     |
| `/pinata list`                       | Aktif olan tüm Piñata'ları listeler.                 | `benthpinata.command.list`      |
| `/pinata kill <id>`                  | Belirtilen ID'deki Piñata'yı sonlandırır.            | `benthpinata.command.kill`      |
| `/pinata killall`                    | Tüm Piñata'ları ve görevleri sonlandırır.            | `benthpinata.command.killall`   |
| `/pinata reload`                     | Eklentinin tüm yapılandırmasını yeniden yükler.      | `benthpinata.command.reload`    |
| `/pinata stats`                      | Kendi Piñata istatistiklerinizi gösterir.            | `benthpinata.stats`             |
| `/pinata stats top <damage\|kills>` | En çok hasar veren veya Piñata öldürenleri listeler. | `benthpinata.stats.top`         |
| `/pinata stats reset <oyuncu\|all>`  | Bir oyuncunun veya herkesin istatistiklerini sıfırlar. | `benthpinata.stats.reset`       |
| **(Tüm Yetkiler)** | Yukarıdaki tüm komutlara erişim sağlar.               | `benthpinata.admin`             |

---

## ⚙️ Yapılandırma Dosyaları
Eklenti, tüm ayarlarını `plugins/BenthPinata/` klasöründeki dosyalarda saklar.

* `config.yml`: Genel ayarlar, Piñata türleri, konumlar, can değerleri, efektler ve otomatik etkinlik sistemi gibi ana yapılandırmaları içerir.
* `messages.yml`: Eklentideki tüm kullanıcıya dönük mesajları, başlıkları ve hologram metinlerini içerir. Tamamen Türkçeleştirilebilir.
* `rewards.yml`: Vuruş başına, hasar eşiğine ve etkinlik sonuna özel tüm ödül komutlarını ve şanslarını içerir.
* `abilities.yml`: Piñata türlerine özel yeteneklerin tanımlandığı dosyadır.
* `stats.yml`: Oyuncu istatistiklerinin saklandığı veri dosyasıdır. Bu dosyayı manuel olarak düzenlemeniz önerilmez.

---

## 🛠️ Örnek Yapılandırma
`config.yml` dosyasının ne kadar esnek olduğunu göstermek için küçük bir örnek:
```yaml
pinata-types:
  # 'zindan' isminde, parlak ve yapay zekası olmayan bir Zombi Piñata
  zindan:
    spawn-location: 'world;15;70;25'
    health: 250
    entity-type: 'ZOMBIE'
    mob-options:
      is-baby: false
      glowing: true
      custom-name: '&4&lZİNDAN PİÑATASI'
      custom-name-visible: true
      # Bu Piñata'nın sessiz olmasını ve hareket etmemesini sağlıyoruz.
      nbt-data: '{ "Silent": 1b, "NoAI": 1b }'
```

---

## 🧩 PAPI Desteği
Eğer sunucunuzda `PlaceholderAPI` kuruluysa, aşağıdaki placeholder'ları kullanarak BenthPinata verilerini diğer eklentilerde gösterebilirsiniz.

### Oyuncu İstatistikleri
* `%bp_stats_damage%` - Oyuncunun toplam Piñata hasarını gösterir.
* `%bp_stats_kills%` - Oyuncunun toplam Piñata öldürme sayısını gösterir.

### Liderlik Tablosu
* `%bp_top_damage_<rank>_name%` - Hasar sıralamasındaki `<rank>`. oyuncunun adını gösterir (örn: `%bp_top_damage_1_name%`).
* `%bp_top_damage_<rank>_value%` - Hasar sıralamasındaki `<rank>`. oyuncunun hasarını gösterir.
* `%bp_top_kills_<rank>_name%` - Öldürme sıralamasındaki `<rank>`. oyuncunun adını gösterir.
* `%bp_top_kills_<rank>_value%` - Öldürme sıralamasındaki `<rank>`. oyuncunun öldürme sayısını gösterir.

---

## 🗓️ Gelecek Planları (Roadmap)
BenthPinata projesi aktif olarak geliştirilmektedir. İşte planlanan bazı özellikler:

-   [ ] Daha fazla Piñata yeteneği (ateş çemberi, zehirli alan vb.).
-   [ ] Oyun içi GUI ile Piñata türü oluşturma/düzenleme editörü.
-   [ ] Birden fazla hologram eklentisi için destek.
-   [ ] Diğer geliştiricilerin kendi sistemlerini entegre etmesi için bir Developer API.

---

## 💬 Destek ve Geri Bildirim

Bir hata bulursanız veya bir özellik öneriniz varsa, lütfen [GitHub Issues](https://github.com/bentahsin/BenthPinata/issues) sayfası üzerinden bir bildirim oluşturun.

---

## 🤝 Katkıda Bulunma
Projeye katkıda bulunmak isterseniz, her zaman bekleriz! Lütfen bir "fork" oluşturun ve değişikliklerinizi içeren bir "pull request" gönderin.

---

## 📄 Lisans

Bu proje, özgür ve açık kaynaklı yazılımı destekleyen [MIT Lisansı](LICENSE) altında lisanslanmıştır. Detaylar için `LICENSE` dosyasını inceleyebilirsiniz.