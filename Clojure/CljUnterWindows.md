# Arbeiten mit Clojure unter Windows ...

... auf den NORDAKADEMIE-Rechnern.

## Benutzung der Repl
### Installation Clojure/Leiningen

1. Kopieren Sie sich das Verzeichnis `P:\nt\clj` mach Z:\ (im
Windows-Explorer nur per Drag & Drop möglich)
    Ergebnis: `Z:\CLJ`
2. cmd (Windows-Shell) starten
3. Laufwerk Z: wählen
4. ins Verzeichnis CLJ wechseln: `cd CLJ`
5. `lein self-install` aufrufen
2. Starten Sie `lein repl`
3. Geben Sie Clojure-Ausdrücke ein!

Wenn Sie für das Schreiben von Funktionen einen Texteditor Ihrer Wahl
bevorzugen, erzeugen Sie z. B. eine Textdatei `function.clj`. Die
können Sie dann am Repl-Prompt mit der Funktion `load-file` laden:

>> `> (load-file "function.clj")`

## Verwenden von Light Table

1. Doppelklick auf Lighttable.exe
2. ein neues Fenster öffnen
2. Menüpunkt `View -> Commands -> Editor` wählen und dort: `Set current editor syntax -> Clojure`
4. Clojure-Ausdrücke eingeben und mit `Strg/Enter` auswerten

## Alternative zu LightTable: Clooj
Unter T./Dozenten/Brauer finden Sie die Datei

    clooj-0.5-standalone.jar

Am Besten kopieren Sie sich die Datei in Ihr CLJ-Verzeichnis. Durch
Doppelklick wird Clooj gestartet, eine leichtgewichtige Clojure IDE,
die sich weitgehend selbst erklärt.
