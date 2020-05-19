## Build keretrendszer - Gradle
A Gradle már a be volt üzemelve a projekthez, viszont nagyon régi verziót használt, sok deprecated elemet tartalmazott. Ezért, hogy a build gond nélkül működhessen és hogy tudjunk használni pl: _JaCoCo_-t, frissítettük a **6.4**-es verzióra.

A módosítás magába foglalta:
*  a gradle/wrapper/gradle-wrapper.properties fájlban a _verziószám_ módosítását,
*  a build.gradle-ben a függőségek esetében a _compile_ kulcsszavak _implementation_-re történő cserélését és a _task_ wrapper módosítását.

## Build és tesztek futtatása a frissítés után
Miután sikeresen lefutott a build, a tesztek futtatása hibát dobott. Ez azért volt, mivel az egyik tesztben (_testWriteMap_) Date objektumot is szerializál az _RDFMapper_, viszont a szerializált dátum tartalmaz időzóna információt is. A Date osztály nem kezeli az időzónákat, így a helyi időzóna szerint írta ki a long paraméterként kapott időpontot. Mivel az összehasonlítást végző _Models_ osztály _isomorphic_ függvénye nem veszi figyelembe az időzónákat, ezért elbukott a teszt, ha nem az adatfájlban lévő időzóna beállításokkal futtattuk a teszteket. 

Erről [issue](https://github.com/stardog-union/pinto/issues/24) is található a projekt eredeti github repository-jában.

Ennek a problémának a megoldására módosítottuk a forrást az UTC+1 időzónára, így a helyi gépeken sikeresen lefutottak a tesztek. 

## CI bekötése - Actions
A _gradle.yml_ fájlban található a GitHub Actions konfigurációja, mely gradle build-et futtat minden a _master_ branch-re érkező:
*  pull request és
*  push esetén.

Miután sikerült beállítani az Actions-t, a gradle-nél kijavított teszteset a CI-ban futtatva már nem működött. Ezért a releváns teszteseteknél (_testWriteMap_ és _testReadMap_) feltételes futtatást alkalmazunk (_Assume.assumeTrue_), mely feltétel szerint csak akkor futhat le a teszt, ha az UTC+1-es időzóna beállításokkal rendelkezik a rendszer.

A feltételt a következő osztály ellenőrzi:

![A feltételt ellenőrző osztály](/doc/images/utcchecker.png)

Az Actions bekötése nehezen ment, mivel egy külön branch-en csináltam a yml konfigurációt (_actions-beuzemelese_) és külön branch-en terveztem csinálni az actions trigger-eléséhez szükséges módosítások elvgézését (_actions-teszt_). Azonban a folyamat lépéseinek sorrendját nem jól határoztam meg és azon a branch-en (_actions-teszt_) nem volt meg a yml fájl (még korábban a _master_-ről hoztam létre, nem az _action-beuzemeles_-ről, és akkor még a _master_-en nem létezett a yml), ezért hiába commit-oltam, nem futott a CI.
Ennek a tesztelése miatt sajnos nem volt szép a repo struktúrája az elején, de az ehhez vezető hibákat a többi feladat esetében már nem követtem el.
