package main

import (
    "fmt"
    "go/ast"
    "go/parser"
    "go/token"
    "os"
    "go/format"
)


func main() {
    if len(os.Args) != 2 {
        fmt.Printf("usage: astprint <filename.go>\n")
        return
    }

    // Создаём хранилище данных об исходных файлах
    fset := token.NewFileSet()

    // Вызываем парсер
    if file, err := parser.ParseFile(
        fset,                 // данные об исходниках
        os.Args[1],           // имя файла с исходником программы
        nil,                  // пусть парсер сам загрузит исходник
        parser.ParseComments, // приказываем сохранять комментарии
    ); err == nil {
        if format.Node(os.Stdout, fset, file) != nil {
            fmt.Printf("Formatter error: %v\n", err)
        }
        // Если парсер отработал без ошибок, печатаем дерево
        ast.Fprint(os.Stdout, fset, file, nil)

    } else {
        // в противном случае, выводим сообщение об ошибке
        fmt.Printf("Error: %v", err)
    }
}