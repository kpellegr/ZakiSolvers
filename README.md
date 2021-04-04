Dit is een kleine library met naïeve solvers voor het spel "Cijfers en Letters".

Deze repo bevat de library "ZakiSolver" en een test app die je kan gebruiken om de solvers te testen.

## Installeren

- Importeer de folder 'ZakiSolver' uit de testapp.
    - New > Import Module
    - ZakiTestApplication/ZakiSolver aanklikken (je ziet module :ZakiSolver staan in het import window)
    - Ok
- Je project bevat nu een tweede module: "ZakiSolver"
    - Let op: je hebt nu ook een extra build.gradle file (eentje voor je app en eentje voor de module)
    - Voeg volgende dependency toe aan de build.gradle file voor de app:
    - `implementation project(path: ':ZakiSolver')`
    - (je kan ook de Solver proberen te gebruiken; Android Studio zal die code dan rood markeren. In het context menu zal hij voorstellen om de dependency zelf toe te voegen)
    - Eens de dependency toegevoegd is je code nog steeds rood, maar moet je enkel de import nog doen (via bvb Option-Enter)

## Hoe te gebruiken

- Beide solvers werken op een gelijkaardige manier (en zijn afgeleid van de Solver-klasse)
    - Je maakt een nieuwe Solver aan
    - `Solver solver = new NumberSolver()`
    - Je stelt de solver in met "setInput".
    - `solver.setInput(inputNumbers, target)`
    - Met solver.solve() kan je de solver laten lopen. Deze taak loopt op een background thread. Wanneer die klaar is, roept ze een callback op met een lijst met resultaten. Je geeft aan solve() dus een callback mee.
    - `solver.solve(results → { 
        // doe wat je wil met de resultaten 
    }`
- De NumberSolver heeft een lijst met getallen nodig en een target getal.
- De LetterSolver heeft enkel een lijst met letters nodig. Merk op dat de lettersolver ook een dictionary nodig heeft. Ik heb een woordenlijst in de resource folder gestoken (onder de subfolder 'raw'). Deze moet wel manueel geladen worden (omdat hiervoor een Context nodig is, en die heb ik niet binnen de library). Om de LetterSolver te gebruiken moet je dus eerst nog dit doen:
- `solver.loadDictionary(context, resID)`
- De context kan je activity zijn. Vanuit je fragment kan je 'requireActivity()' gebruiken.
- De resource-ID volgt dezelfde naamgeving als layouts en strings (maar dan voor de 'raw' folder). De default dictionary is dan ook: R.raw.dictionary_nl
- `solver.loadDictionary(requireActivity(), R.raw.dictionary_nl)`
- Als je wil kan je dus ook je eigen dictionary toevoegen. Dat is gewoon een woordenlijst met één woord per lijn. Je kan die dan in de 'raw' folder slepen en de resource ID aanpassen.

## Beperkingen

- De solvers lopen op een background thread, maar er is geen manier om ze te onderbreken.
- Er is ook geen manier om tussentijdse updates te krijgen.
- De LetterSolver is uiteraard ook beperkt door de woordenlijst. In de default lijst zitten bvb geen meervouden of vervoegingen.
- De NumberSolver heeft volgende beperkingen:
    - In tussentijdse berekeningen zijn kommagetallen toegelaten. Dus 5/2*4 is een geldige oplossing om aan 10 te geraken. Ook negatieve getallen zijn toegelaten: 3-10+20 is geldig om aan 13 te geraken.
    - Er is geen optimalisatie voor commutativiteit: 2+3 en 3+2 worden als twee unieke oplossingen gezien.
    - Omwille van performantie-redenen geeft de NumberSolver alleen de beste oplossingen (zodra die een betere oplossing vindt, gooit ie de andere oplossingen weg). Als er bvb één manier is om exact 532 te bereiken, en 10 manieren om aan 531 te geraken, dan zal de solver enkel de oplossing voor 532 tonen.

## Performantie

- De solvers doorlopen alle mogelijke permutaties van de input om een oplossing te zoeken.
- Als je 5 letters aan de LetterSolver meegeeft, dan zijn er 5*4*3*2*1 = 120 mogelijke woorden die met het woordenboek moeten vergeleken worden. Het aantal combinaties voor de LetterSolver is gelijk aan het aantal input-letters faculteit. Voor 8 letters zijn er 8! = 40320 mogelijk woorden. De faculteit-functie gaat heel erg snel de hoogte in.
- Merk op dat **elk** van die 40320 mogelijkheden dan ook vergeleken wordt met een woordenlijst met 29406 woorden 😎
- Voor de NumberSolver is er geen woordenlijst, maar het aantal combinaties stijgt daar wel nog sneller. Als ik 6 getallen neem, dan kan ik die in 6! = 720 mogelijke combinaties samenzetten. Maar ik moet ook de bewerkingen meetellen. Voor twee getallen zijn er dus 2 * 1 * 4 mogelijke combinaties:  a+b, a-b, a*b, a/b, b+a, b-a, b*a, b/a.
- Voor 6 getallen levert dat 6! * 4 * 5! = 345600 mogelijke combinaties op. Met 8 getallen kom je al snel aan een klein miljard mogelijkheden.
