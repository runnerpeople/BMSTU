package main

import (
	"fmt"
	"go/ast"
	"os"
	"go/token"
	"go/parser"
	"go/format"
)

func removeAssignment(file* ast.File) {
	ast.Inspect(file,func (node ast.Node) bool {
		if blockStmt, ok := node.(*ast.BlockStmt); ok {
			for i,elem := range blockStmt.List {
				switch elem.(type) {
					case *ast.AssignStmt:
						if assignments, ok := blockStmt.List[i].(*ast.AssignStmt); ok {
							if assignments.Tok == token.DEFINE {
								var idents []*ast.Ident
								for _, element := range assignments.Lhs {
									if element_ident, ok := element.(*ast.Ident); ok {
										idents = append(idents, element_ident)
									}
								}
								blockStmt.List[i] = &ast.DeclStmt{
									Decl: &ast.GenDecl{
										Tok: token.VAR,
										Specs: []ast.Spec{
											&ast.ValueSpec{
												Names:  idents,
												Type:   nil,
												Values: assignments.Rhs,
											},
										},

									},
								}
							}
						}
				}
			}
		}
		return true
	})
}

func main() {
	if len(os.Args) != 2 {
		return
	}

	fset := token.NewFileSet()
	if file,err := parser.ParseFile(fset,os.Args[1],nil,parser.ParseComments); err==nil {
		removeAssignment(file)

		if format.Node(os.Stdout, fset, file) != nil {
			fmt.Printf("Formatter error: %v\n", err)
		}
		ast.Fprint(os.Stdout, fset, file, nil)
	} else {
		fmt.Printf("Errors in %s\n", os.Args[1])
	}


}