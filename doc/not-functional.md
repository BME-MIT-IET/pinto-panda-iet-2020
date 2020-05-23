# Nem-funkcionális jellemzők vizsgálata

## Használhatóság

### Build

A libary-hez tartozik egy readme, ahol a build és a teszt futtatás egyszerű parancsai le vannak írva, azonban a projektépítő eszközről más információ (például link nincs), igaz a Gradle elég elterjedt és ismert, így ez alapvetően nem okoz problémát.
A Gradle használata egyszerű és minden alapesetben szükséges konfigurációt a libary beállít, így egy rövid parancs elegendő.

### Run

A használatához kapunk egy gyors és egyszerű példát, illetve az alkalmazás tesztjeinek megtekintésével további példákhoz tudunk jutni, ezzel megkönnyítve a libary használatának megértését.



## Teljesítmény

### Build

![Teljesítmény javaslatok](/doc/images/suggestions.png)
Egy nagyon régi (2.0) Gradle verzió volt a konfigurációban, ez jelentősen ronthatja a teljesítményt (és hibát is okozhat), ezt a csapat javította a legújabb verzióra (6.4.1).
A Gradle Deamon nincs letiltva, ami a teljesítményt javítja
A párhuzamos végrehajtás nincs engedélyezve ezáltal a Gradle Enterprise ajánlásai szerint hosszabb a build ideje, azonban a timeline-t és az időket megnézve ilyen és olyan esetben, nem látjuk a párhuzamos futtatás nyomait. (Ennek engefélyezéséhez a gradle.properties fájlba be kéne írni ezt: org.gradle.parallel=true)
![Nem párhuzamos](/doc/images/not_parallel.png)
![Párhuzamos](/doc/images/parallel.png)

A libary build teljesítményét egy másik projekthez (4-6. gyakorlat) hasonlítva, a következőket állapíthatjuk meg (természetesen ez így nem reprezentatív, de kiindulásnak jó):
![Pinto](/doc/images/pinto_performance.png)
![Másik projekt](/doc/images/other_performance.png)

Összesítve a pinto build ideje rövidebb, a Startup idejénél nincs különbség, a különbség nagy része a Settings-ből adódik ezek alapján a pinto-ban nincs sok "settings script", 
azonban jóval kisebb különbséggel, de a Configuration-ben pont az ellenkezőjét láthatjuk, így itt érdemes megvizsgálni, hogy ezt mi okozza.
A Configuration idejébe a build plugins, build scripts végrehajtása, projekt konfigurálása és a feladat végrehajtási terv elkészítése számít bele, a pluginokat végignézve a projektben 14 plugin tallható, ezek egy része már az általunk hozzáadott pluginok (jacoco, com.gradle.enterprise...), ezek idejét (Settings+Configuration) megvizsgálva látható, hogy ebből a plugin-okra fordított szinte egész időt az ezek megállapításához használt Gradle Enterprise okozza, így ezt nem érdemes használni, ha nem szükséges.
![Pluginok ideje](/doc/images/plugins_time.png)

Még a Task execution-t érdemes jobban megvizsgálnunk, itt azt láthatjuk, hogy a task-ok naprakészen tartásával jelentős, a task végrehajtásra fordított idő 91%-át spórolhatjuk meg.
![Feladat végrehajtás](/doc/images/task_execution.png)


### Run

A futtatás teljesítményét a tesztek segítségével néztem meg. (https://scans.gradle.com/s/gv4xghc7mxa2m/tests)
A legtöbb teszt pár ezredmásodpercen belül lefutott, így közöttük nem volt nagy különbség és érdemben nem is lehetett vizsgálni, azonban voltak ehhez képest kiemelkedőbb értékek is ezeket próbáltam vizsgálni.

Az RDFMapperTests lefutási idejeit végignézve megfigyelhető, hogy milyen típusok hatására mekkora időbeli különbségek vannak a különböző típusok és adatok írása/olvasása között.

![RDFMapperTests tesztejei](/doc/images/mapper_test.png)

Az RDFMapperBuilderTestsIET tesztjeit vizsgálva megfigyelhető, hogy a különböző RDFMapperBuilder-ek között mekkora különbség van és míg egy sima értékkel vagy namespace-el lefut 1-2 ezred másodpercen belül, addig egy Map esetében ez az érték már nagyjából 50-szer annyi, ebből nagyon jól látszik, hogy egy ilyen többszörösen összetett érték (nem meglepő módon) jelentősen megnövelik a futási időt.

![Builder tesztek](/doc/images/builder_test.png)


## Gradle Scans
https://scans.gradle.com/s/quyz3hgydd2tc
https://scans.gradle.com/s/vxormbssi6zma

https://scans.gradle.com/s/phahp4uzm3rno
https://scans.gradle.com/s/gv4xghc7mxa2m
https://scans.gradle.com/s/ag2tczuvft7m4