##  Manuális kód átvizsgálás
A projektben az úgyevezett üzleti logika egy jelentős részét az RDFMapper osztály valósítja meg, ezért választottam ezt az elemzéshez. Több szempont szerint is megvizsgáltam az osztályt (részeltesen: [issue](https://github.com/BME-MIT-IET/pinto-panda-iet-2020/issues/16)):
a függvények belsejében zavaróak az elnevezések,
nagyon jók a kommentek az egyes függvények fejléceinél, viszont a logikát leíró függvények hiányoznak, ezért nehezen érthetőek,
nagyon nagy az osztály, szerintem érdemes lenne modulariálni, ha magát az osztályt nem is, de a függvényeket midnenképpen.

Összegezve: sok időbe telt megérteni az osztály működését, ez egy nagy negatívum szerintem. Ennek ellnére jól használható az osztály, a builder segítségével kevés kóddal elkészíthető a megfelelően felparaméterezett osztály és a bean-ek szerializálása/deszerializálása egy-egy függvényhívással elvégezhető.

##  Statikus ellenőrzési eszköz használata - SonarQube/Cloud
A SonarCloud statikus analizátort használtuk a kód átvizsgálására. Használata egyszerű, a gradle-be integrálható, így a CI-ba bekötve minden master-re érkező pull request és push hatására lefut. A kódfedettség mérésére JaCoCo-t használ.

Az elemzés (részeltesen: [issue](https://github.com/BME-MIT-IET/pinto-panda-iet-2020/issues/16)) során a következőket figyeltem meg:
* a jelentősebb hibák közül a deprecated függvények használata volt a legszembetűnőbb (ezt kritikusként jelölte meg a sonar): mivel a könyvtár master-en lévő verziója már jó pár éves, ezért szükség lenne az abban használt könyvtárak felülvizsgálatára, esetlegesen az azok új verziójára törénő frissítésre. Ezzel potenciális sebezhetőségeket lehetne elkerülni, amik egy-egy támadásnak adnak lehetőséget,
* kiemelkedően magas azoknak a jelzéseknek a száma, amik az egyes függvények komplexitását mérték: sok a hosszú függvény, melyek működésének a megértését a hosszuk nagy mértékben nehezíti.

A felsoroltakon kívül nem állapított meg a sonar jelentősebb hibát.