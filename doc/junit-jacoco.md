# JUnit - Jacoco - Mockito

## Jacoco

A pinto library-hez már meg volt írva több, mint 50 teszteset JUnit-al. Az első feladatom ezek kódfedettség vizsgálata volt. Ehhez jacoco-t használtam, amelyet egy sorban hozzá lehetett adni a build.gradle-höz. Majd 
`gradle build jacocoTestReport`-al a reportot el lehetett készíteni.

Az eredeti kódefedettség a gyökér könyvtáron és a lényegi kódrészeken:

![Kodfedettseg gyokeren](/doc/images/fedettseg-gyoker-before.png)

![Kodfedettseg pinto konyvtaron](/doc/images/fedettseg-pinto-before.png)

## JUnit

Először olyan unit teszteket írtam, amelyek a kódfedettséget növelték. Később olyanokat amelyek olyan funkciókat teszteltek, amelyeket az erdeti tesztek nem.

Az kihagyott funkciók tesztelése során felmerült egy hiba a kódban. A byte típus írásakor a hosszú if-else ágak között kimaradt a byte típus ellenőrzése, azonban beolvasáskor implementálva volt. Így a byte típus kiírása utáni olvasás hibához vezetett, mivel nem primitív jól ismert byte-ként kezelte a kiírt értéket, hanem ismeretlen objektumként.

A hibát sikerült kijavítani a library-ben, illetve két új tesztet is írtam, amely ellenőrzi, hogy az összes jól ismert primitív típust felismeri a library amikor kiírja azokat.

## Mockito

A unit tesztek között sok olyan ideiglenes osztály volt, amelyeket csak a tesztek használtak, de valójában nem sok értelmük volt. Ezért úgy gondoltam egyszerűbb lenne Mockito-val beállítani a függvények visszatérési értékeit, így kiküszöbölve a furcsa osztályok használatát.

Mivel a library-ben sok a privát és statikus függvény, valahogy ezeket is kellett mock-olni, viszont a Mockito ezt nem támogatja. Ezért a PowerMock-t is használtam ami már támogatja ezeket.

## Kódfedettség a feladat végén

![Kodfedettseg gyokeren](/doc/images/fedettseg-gyoker-after.png)

![Kodfedettseg pinto konyvtaron](/doc/images/fedettseg-pinto-after.png)