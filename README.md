


### Windows
1. Compilar
    ```shell
        javac -cp "lib/jgraphx.jar" -d out src/structures/*.java src/model/*.java src/io/*.java src/algorithms/*.java src/ui/*.java src/Main.java
    ```
2. Executar
    ```shell 
        java -cp "out;lib/jgraphx.jar" Main
    ```
### Linux/macOS
1. Compilar
    ```shell
        javac -cp "lib/jgraphx.jar" -d out $(find src -name "*.java")
    ```
2. Executar
    ```shell
        java -cp "out:lib/jgraphx.jar" Main
    ```




    