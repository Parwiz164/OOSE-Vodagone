

























Inhoudsopgave
Inleiding	3
Casus: Vodagone	4
Package Diagram	5
Deployment Diagram	6









































Inleiding

Dit is een opleverdocument voor de Programmeeropdracht EAI voor het vak DEA.
Dit document bevat een korte beschrijving over de casus Vodagone REST,
Package en Deploymentdiagram.







































 
Casus: Vodagone
Vodafone en Ziggo hebben de handen ineengeslagen en werken gezamenlijk aan een app (Vodagone) waarmee een klant inzicht kan krijgen in de abonnementen die bij de bedrijven zijn afgesloten. Ze willen eerst een deel van de back-end ontwikkelen en deze testen via een eenvoudige webapplicatie alvorens over te gaan tot de ontwikkeling van de app.






















 
Package Diagram
 

Het packagediagram is opgesteld in lagenmodel van Flower. De lagen zijn
•	Presentation.Rest
•	DAO
•	Domain
•	JDBC
Deployment Diagram
 
De Browser communiceert via REST/HTTP met de Appache Tomee Server. MySQL Database Server communiceert via JDBC-driver met de Appache Tomee Server.
