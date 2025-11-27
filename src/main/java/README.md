Am folosit functia de Copilot Autocomplete pentru a-mi usura munca si a completa mai rapid codul de tip boilerplate.
Am intrebat si am cerut sugestii de la LLM pentru urmatoarele situatii:
(1): daca este OOP like sa folosesc un switch case pentru a-mi instantia obiectele
Mi-a zis ca da este chiar si un design pattern, FactoryPattern, asa ca mi-am numit si clasele de instantiere subiectiv.
(2): daca este ok sa am toate comenzile in clasa robot si i-am zis ca nu pare foarte enterprise level
si l-am intrebat daca exista vreun pattern de comenzi si mi-a zis ca da, chiar CommandPattern.
Si mi-a zis ca in mare parte am o interfata cu o metoda care trebuie implementata intr o clasa 
care are acea comanda specifica.
(3): i-am mai cerut clarificari despre cum functioneaza scrierea in file-urile de tip JSON, m-a ajutat cu scrierea.
(4): mi-a mai zis de NodeArray, put, set, valueToTree care converteste obiectul intr-o structura arborescenta pentru JSON.
(5): l-am mai intrebat despre cum se poate itera printr-un ENUM.

IMPLEMENTAREA TEMEI: 
Am in primul rand clasa Entity care este clasa parinte pentru toate entitatile.
Aceasta are campul mass, type si  name comun, am facut clase copii pentru Soil, Air si Animal.
Animal are de asemena doi copii, MeatEater si Vegetarians.
Fiecare subclasa, adica Parasites si Carnivores se extind din MeatEater, care implementeaza metoda de hranire
Iar pentru restul la fel, implementeaza celalalt algoritm de hranire pentru ca nu mananca animale
Nu am facut nicio ierarhie de clase pentru plant si water pentru ca nu a fost nevoie.
Mai ales la plant, chiar daca sunt de tip diferit, nu am vreo implementare diferita sau metode speciale
Doar campuri schimbate, clasele care se extind sunt de asemenea abstracte, facand ca implementarea metodelor diferite
sa fie obligatorie.
Mai am harta care are este o matrice, fiecare pozitie din matrice are un cell care are un loc rezervat pentru
fiecare tip de etitate. MapCell este o clasa interna a hartii, deoarece este necesar ca harta sa existe deja.
In fiecare celula, am de asemena si un entities count pentru a tine evidenta de cate entitati sunt in celula.
INTERACTIUNEA DINTRE ENTITATI:
In fiecare tip de entitate care poate fi scanat (Animal, Plant, Water), am implementat cu interfata Updatable 
fiecare metoda de UpdateMapWithScanObject care face interactiunile dintre entitati, la fiecare timestamp
din switch case ul din clasa Robot, se face interactiunea, de asemena in charge robot, se incepe un loop care 
foloseaste metoda respectiva. Metoda este in Map care cauta fiecare obiect scanat si apeleaza metoda de interactiune 
din fiecare.
Aceste interactiuni cu harta se intampla la fiecare iteratie din switch case ul din clasa Robot.
IERARCHIA DE LOGICA: 
Robot care are toate comenzile si simularea -> harta -> celula -> entitati.
Cand se face o noua simulare, doar se schimba harta si se reseteaza campurile robotului.
In clasa robot am si o metoda care intoarce un String pentru cele mai des intalnite, de isCharging si StartedSimulation
EXPLICAREA PENTRU FIECARE CERINTA:
1. Start simulation - in clasa robot am un boolean care verifica daca este simularea pornita sau nu.
In main am si un contor care verifica daca sunt necesare doua simulari, tin minte o harta secundara si un nivel de energie
De asemenea erorile sunt tratate de catre acest identificator de daca a inceput simularea sau nu.
2. End simulation face cam acelasi lucru, doar ca il pune ca fals.
3. PrintMap - printeaza numarul de entitati din contorul explicat mai sus pentru entitati si afiseaza calitatea celulei.
4. PrintEnvConditions printeaza ceea ce este in celula in care este robotul, get Celula in care este robot si print all.
5. Move robot se muta in functie de probabilitate, aceasta metoda este in clasa map si se verica celula cu probabilitatea
cea mai buna. Daca are energie se muta.
6. Scan Object sunt scanate obiecte, este pus flagul de isScanned, sunt adaugate in inventar si in inventarul
de baza de date, nu a fost foarte clar in enunt cum ar trebui sa fie facuta baza de date, am un ArrayList pentru
inventar si inventarul de baza de date. Este apelata metoda din class cum scrie si mai sus pentru a intretine interactiunile
O problema mare pe care am avut-o a fost mai ales la move animal pentru ca "daca Exista" nu este deloc clar, acest verb este 
folosit in foarte multe parti ale temei si nu pare ca reflecta foarte bine faptul ca se refera la entitati scanate !!!!!!!!!.
Scan robot functioneaza sa si dea seama de care obiecte e in functie de ce campuri au None sau nu, smell color ... .
7. Charge Robot se incarca bateria si se executa interactiunile in timpul in care este pauza.
8. LearnFact, adauga pur si simplu informatia, nu stiu ce informatii suplimentarea ar trebui sa adaug despre implementare.
Este destul de evident ce face.
9. Improve Env, se uita in inventar, mareste campurile pentru ce tip de improvement este si dupa este eliminat din inventar.

O problema pe care am avut-o a fost la partea de mancat pentru animal, nu foloseam o abordare POO, verificam in functie de 
tipul animalului, asa ca mi am facut clasele herbivores si meatEaters care implementau metoda de hranire a animalului.
Deci cand se apela animal.feed se apeleaza la runtime metoda corecta in functie de tipul animalului, fara sa fie 
necesare verificari aditionale.

Mi-am facut si o clasa EntityParser pentru a nu supraincarca main ul cu citirea de la input si parsing-ul.


