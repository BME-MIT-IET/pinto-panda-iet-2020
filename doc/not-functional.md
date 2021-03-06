# Nem-funkcionális jellemzők vizsgálata

## Használhatóság

### Build

A libary-hez tartozik egy readme, ahol a build és a teszt futtatás egyszerű parancsai le vannak írva, azonban a projektépítő eszközről más információ (például link nincs), igaz a Gradle elég elterjedt és ismert, így ez alapvetően nem okoz problémát.
A Gradle használata egyszerű és minden alapesetben szükséges konfigurációt a libary beállít, így egy rövid parancs elegendő.

### Tesztek

A használatához kapunk egy gyors és egyszerű példát, illetve az alkalmazás tesztjeinek megtekintésével további példákhoz tudunk jutni, ezzel megkönnyítve a libary használatának megértését, így először a teszteket vizsgáltam meg.

A teszteket megnézve elég hamar észrevehető, hogy nagyobb figyelmet és energiát azt nem az adott helyen történő használat, hanem a hozzá szükséges megfelelő adatosztályok elkészítése igényli, ugyanis bizonyos függvények felülírása is szükséges a konstruktor., valamint getterek és setterek elkészítésén túl. Azonban a libary readme-je ehhez is mutat példát, valamint számos másik példa is található a tesztek között, de egyik helyen sincs hozzá semmilyen magyarázat, így jelentősen nehezebb megérteni, hogy most az adott függvény felülírására vagy az adott kódsorra miért van szükség.

Sajnos a tesztek kommentezettségének hiánya nem csak az adott helyen fordul elő, hanem az összes teszt kommentezettsége is csak 2%-os és ez is csak pár fájlból jön össze, ugyanis a legtöbben egy sor kommment se található.

![Komment statisztikák a tesztekben](/doc/images/comments_stat.png)

A többi fájl esetében már sokkal jobb a kommentek aránya és az src mappában található java fájloknál már eléri a 36%-ot is, így ott nincs ilyen probléma.

![Komment statisztikák az src-ben](/doc/images/comments_stat_src.png)

### Libary használata

A használathoz nagy segítség a már említett readme, azonban ott az rdf4j keretrendszer linkje hibás volt, így ezt javítottam.  
A használata a leírás alapján egy-egy változós oszályok esetében egészen egyszerű, csupán a megfelelő (már a teszteknél említett) adatosztály létrehozására van szükség, illetve ahol el akarjuk végezni a folyamatot, ott az egy soros példa kódból az osztály és a megfelelős szöveg/változó kicserélése szükséges.
Azonban összetettebb esetekben már a tesztek megtekintésére és nagyobb átgondolásra van szükség.

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

Összesítve a Pinto build ideje rövidebb, a Startup idejénél nincs különbség, a különbség nagy része a Settings-ből adódik ezek alapján a Pinto-ban nincs sok "settings script", 
azonban jóval kisebb különbséggel, de a Configuration-ben pont az ellenkezőjét láthatjuk, így itt érdemes megvizsgálni, hogy ezt mi okozza.
A Configuration idejébe a build plugins, build scripts végrehajtása, projekt konfigurálása és a feladat végrehajtási terv elkészítése számít bele, a pluginokat végignézve a projektben 14 plugin tallható, ezek egy része már az általunk hozzáadott pluginok (jacoco, com.gradle.enterprise...), ezek idejét (Settings+Configuration) megvizsgálva látható, hogy ebből a plugin-okra fordított szinte egész időt az ezek megállapításához használt Gradle Enterprise okozza, így ezt nem érdemes használni, ha nem szükséges.

![Pluginok ideje](/doc/images/plugins_time.png)

Még a Task execution-t érdemes jobban megvizsgálnunk, itt azt láthatjuk, hogy a task-ok naprakészen tartásával jelentős, a task végrehajtásra fordított idő 91%-át spórolhatjuk meg.

![Feladat végrehajtás](/doc/images/task_execution.png)


### Tesztek

A futtatás teljesítményét először a tesztek segítségével néztem meg. (https://scans.gradle.com/s/gv4xghc7mxa2m/tests)
A legtöbb teszt pár ezredmásodpercen belül lefutott, így közöttük nem volt nagy különbség és érdemben nem is lehetett vizsgálni, azonban voltak ehhez képest kiemelkedőbb értékek is ezeket próbáltam vizsgálni.

Az RDFMapperTests lefutási idejeit végignézve megfigyelhető, hogy milyen típusok hatására mekkora időbeli különbségek vannak a különböző típusok és adatok írása/olvasása között.

![RDFMapperTests tesztejei](/doc/images/mapper_test.png)

Az RDFMapperBuilderTestsIET tesztjeit vizsgálva megfigyelhető, hogy a különböző RDFMapperBuilder-ek között mekkora különbség van és míg egy sima értékkel vagy namespace-el lefut 1-2 ezred másodpercen belül, addig egy Map esetében ez az érték már nagyjából 50-szer annyi, ebből nagyon jól látszik, hogy egy ilyen többszörösen összetett érték (nem meglepő módon) jelentősen megnövelik a futási időt.

![Builder tesztek](/doc/images/builder_test.png)

### Libary használata

Első körben a leírásban szereplő Person() osztályt használtam és a lefutási időt figyeltem különböző számú írás/olvasás esetén.
(Az idők számításánál az írások/olvasások nélküli időt kivontam az írásokkat+olvasásokkat tartalmazó időből, hogy más dolgok minél kevésbé befolyásolják a számokat, illetve minden esetben 3x futtattam a minél pontosabb eredményért átlagoltam.)

Egy Stringet tartalmazó osztály esetében:

RDF olvasás | RDF írás | Idő (s) | Idő/Művelet (ms)
------------ | ------------- | ------------- | -------------
0 | 1 | 0,19 | 189,67
0 | 100 | 0,25 | 2,49
0 | 10.000 | 0,45 | 0,04
0 | 1.000.000 | 3,98 | 0,00
1 | 0 | 0,01 | 12,00
100 | 0 | 0,01 | 0,15
1000 | 0 | 0,02 | 0,02
10.000 | 0 | 0,10 | 0,01
1.000.000 | 0 | 1,13 | 0,00

Ahogy látható az RDF olvasás jelentősen kevesebb időt vesz igénybe, mint az írás és ahogy növeljük a műveletek számát, jelentősen csökken az egy-egy műveletre jutó idő átlaga.
És míg egy-egy olvasi és írási idő között nagyjából 16-szoros különbség van, addig ez 10.000 esetben már csak 4,5-szeres, így látható, hogy minél több írást hajt végre annál kisebb lesz az írások és az olvasások közötti idő

Ezek után megvizsgáltam különböző típusok írások és olvasási idejét (1.000.000 írás/olvasás):

Típus| Művelet | idő (s)
------------ | ------------- | -------------
String | írás | 3,98
Integer | írás | 3,97
Long | írás | 4,04
6 primitív | írás | 9,69
String | olvasás | 1,13
Integer | olvasás | 1,33
Long | olvasás | 1,43
6 primitív | olvasás | 6,60

A különböző típusok között a mérések alapján írás esetén szinte semmi különbség nincs, olvasás esetében pedig egy kicsi különbség van.  
Megvizsgáltam azt is, hogy mi történik, ha egy-egy érték helyett 6 különböző típusú értéket tároló osztályt próbálunk RDF-be alakítani, illetve onnan kiolvasni, ekkor nem meglepő módon megnőnek az idők, azonban míg 6-szor annyi változót tárolunk az írás ideje csak ~2,5-szeresére nő, az olvasás ideje viszont nagyjából 5-szörösére nő (ha a másik 3 mérés átlagát nézzük), azonban itt már számít az adatok típusa, mert ha a String olvasási idejével hasonlítjuk össze, akkor ez a szám már 5,8, így szinte majdnem már 6-ot kapunk, vagyis megállapítható, hogy több érték egy osztályban való tárolásával az írás idejével jelentősen spórolhatunk, viszont az olvasásnál már nincs nagy különbség.

Összehasonlítás képpen ennek a libary használatának a segítségével a tárgy 3. gyakorlatán használt, a Szépművészeti Múzeum műkincseit tartalmazó adatbázis egy részét tartalmazó ontológia fájlméretével közel azonos fájlméretű ontológia generálása nagyjából 1,67 másodperc és az olvasása pedig nagyjából 48,5 perc.

## Stresszteszt

A stresszteszthez megvizsgáltam, hogy különböző méretű osztályok esetében mi történik, ehhez Integer-ek tömbjét vettem és csak egy-egy írást/olvasást végeztem mindig el.

Művelet | Tömb mérete | idő (s) | Idő/Művelet (ms)
------------ | ------------- | ------------- | -------------
írás | 10 | 0,20 | 19,9667
írás | 100 | 0,19 | 1,8567
írás | 10.000 | 0,21 | 0,0213
írás | 100.000 | 0,66 | 0,0066
írás | 1.000.000 | 9,03 | 0,0090
írás | 2.000.000 | 31,35 | 0,0157
írás | 3.000.000 | FAILED | FAILED
olvasás | 10 | 0,00 | 0,4333
olvasás | 100 | 0,01 | 0,0833
olvasás | 1000 | 0,12 | 0,1203
olvasás | 10.000 | 17,25 | 1,7255
olvasás | 100.000 | 1580,63 | 15,8063

Az eddigi tapasztalatoknak megfelelően a kisebb értékeknél itt is az olvasás ideje a kisebb, azonban érdekes módon a nagyobb nagyságrendű értékek felé haladva ez megfordul és 10.000-es teszt olvasása kétszer annyi időbe tellik, mint a 10-szer akkora írása.

**Írás esetében:** A 10, 100, 10.000 írási ideje szinte azonos. Azonban utána jelentősen megnő az írások összesített ideje és 100.000 után az egy műveletre jutó idő is elkezd növekedni, míg 3.000.000 teszten pedig már hibával leállt, ezek alapján az írás a nagyméretű osztályok esetében MEGBUKOTT a stresszteszten.

**Olvasás esetében:** 10 és 100 olvasás esetében ugyanazt láthatjuk, mint írás esetében, vagyis nincs különbség. 
Azonban nagyságrendileg nagyobb számoknál már az eddig tapasztaltakkal ellentétben nem csökken, hanem nő az egy műveletre jutó idő, olyannyira, hogy 100.000-es Integer tömb esetében a 3 teszt helyett csak egyet végeztem, mert (a képen is láthatóan) közel fél órába telt a lefutása, ezek alapján olvasás esetében nagyméretű osztályok esetében MEGBUKOTT a stresszteszten.

![100000-es írási teszt](/doc/images/100000_test.png)


### Tesztekhez használt gép

Processor: Intel Core i7-8750H CPU @ 2.20GHz  
RAM: 20.0 GB  
64 bites Windows operációs rendszer  
Java 1.8.0_251

Ahogy az alábbi ábrán is látható az alkalmazás időnként eléggé kihasználta a gép erőforrásait:  
![CPU használat](/doc/images/CPU.png)

## Gradle Scans
https://scans.gradle.com/s/quyz3hgydd2tc  
https://scans.gradle.com/s/vxormbssi6zma  

https://scans.gradle.com/s/phahp4uzm3rno  
https://scans.gradle.com/s/gv4xghc7mxa2m  
https://scans.gradle.com/s/ag2tczuvft7m4  