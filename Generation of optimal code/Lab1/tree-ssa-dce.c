void print_tree(tree node) {
    if (node == NULL_TREE) return;
    switch (TREE_CODE(node)) {
        case IDENTIFIER_NODE: {
            printf("%s", IDENTIFIER_POINTER(node));
            break;
        }
        case VAR_DECL: {
            printf("%s", IDENTIFIER_POINTER(DECL_NAME(node)));
            break;
        }
        case INTEGER_CST: {
            printf("%d", TREE_INT_CST_LOW(node));
            break;
        }
        case SSA_NAME: {
            if (SSA_NAME_IDENTIFIER(node)) {
                print_tree(SSA_NAME_IDENTIFIER(node));
            }
            printf("SSA version := %d \n", SSA_NAME_VERSION(node));
            break;
        }
        case ARRAY_REF: {
            tree var = TREE_OPERAND(node, 0);
            tree index = TREE_OPERAND(node, 1);
            printf("array_ref [] := \n");
            print_tree(var);
            printf("[");
            print_tree(index);
            printf("]");
            printf("\n");
        }
        case MEM_REF: {
            tree base = TREE_OPERAND(node,0);
            printf("memory_ref * :=");
            print_tree(base);
            printf("\n");
        }
        case ADDR_EXPR: {
            tree base = TREE_OPERAND(node,0);
            printf("addr_ref & :=");
            print_tree(base);
            printf("\n");
            break;
        }
        default:
            break;
    }
}
 
void print_basic_block() {
    basic_block bb;
    gimple_stmt_iterator gsi;
    gimple phi, stmt;
    printf("Info %s: {\n",__PRETTY_FUNCTION__);
 
    FOR_EACH_BB_FN(bb, cfun) {
        edge e;
        edge_iterator ei;
        FOR_EACH_EDGE(e,ei,bb->preds) {
            printf("\tIndex (edge_in): %d\n",e->src->index);
        }
        edge_iterator eo;
        FOR_EACH_EDGE(e,eo,bb->succs) {
            printf("\tIndex (edge_out): %d\n",e->dest->index);
        }
        printf("\t\tBlock index: %d\n", bb->index);
        /* Check all phi functions in the block  */
        for (gsi = gsi_start_phis (bb); !gsi_end_p (gsi); gsi_next (&gsi)) {
            phi = gsi_stmt (gsi);
            printf("\t\t\tPHI Function:= \n");
            print_gimple_stmt(stdout,phi,0,TDF_SLIM);
        }
 
        /* Check all statements in the block.  */
        for (gsi = gsi_start_bb (bb); !gsi_end_p (gsi); gsi_next (&gsi)) {
            stmt = gsi_stmt (gsi);
            int code = gimple_code(stmt);
            switch (code) {
                case GIMPLE_COND: {
                    printf("\t\t\tCOND STMT := \n");
                    tree lhs = gimple_cond_lhs(stmt);
                    tree rhs = gimple_cond_rhs(stmt);
                    print_tree(lhs);
                    print_tree(rhs);
                    print_gimple_stmt(stdout, stmt, 0, TDF_SLIM);
                    break;
                }
                case GIMPLE_ASSIGN: {
                    printf("\t\t\tASSIGN STMT := \n");
                    switch (gimple_num_ops(stmt)) {
                        case 2: {
                            tree lhs = gimple_assign_lhs(stmt);
                            tree rhs = gimple_assign_rhs1(stmt);
                            print_tree(lhs);
                            print_tree(rhs);
                            break;
                        }
                        case 3: {
                            printf("\t\t\tBINARY OPERATION := %s", gimple_assign_rhs_code(stmt));
                            tree lhs = gimple_assign_lhs(stmt);
                            tree rhs1 = gimple_assign_rhs1(stmt);
                            tree rhs2 = gimple_assign_rhs2(stmt);
                            print_tree(lhs);
                            print_tree(rhs1);
                            print_tree(rhs2);
                        }
                        default:
                            break;
                    }
                    print_gimple_stmt(stdout, stmt, 0, TDF_SLIM);
                    break;
                }
                case GIMPLE_RETURN: {
                    printf("\t\t\tRETURN STMT := ");
                    print_tree(gimple_return_retval(stmt));
                    printf("\n");
                    break;
                }
                default:
                    printf("\t\t\tANOTHER STMT := ");
                    print_gimple_stmt(stdout,stmt,0,TDF_SLIM);
            }
        }
    }
    printf("===============\n");
}