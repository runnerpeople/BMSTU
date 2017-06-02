void print_tree(tree node) {
    switch (TREE_CODE(node)) {
        case IDENTIFIER_NODE: {
            printf("%s", IDENTIFIER_POINTER (node));
            break;
        }
        case VAR_DECL: {
            if (DECL_NAME(node))
		        printf("%s", IDENTIFIER_POINTER(DECL_NAME(node)));
	        break;
	    }
        case CONST_DECL: {
            if (DECL_NAME(node))
                printf("%s", IDENTIFIER_POINTER (DECL_NAME(node)));
            break;
        }
        case INTEGER_CST: {
            printf("%ld", TREE_INT_CST_LOW(node));
            break;
        }
        case STRING_CST:
            printf("\"%s\"", TREE_STRING_POINTER (node));
            break;
        case ARRAY_REF: {
            tree var = TREE_OPERAND(node, 0);
            tree index = TREE_OPERAND(node, 1);
            print_tree(var);
            printf("[");
            print_tree(index);
            printf("]");
            break;
        }
        case MEM_REF: {
            tree base = TREE_OPERAND(node, 0);
            printf("*");
            print_tree(base);
            break;
        }
        case ADDR_EXPR: {
            tree base = TREE_OPERAND(node, 0);
            printf("&");
            print_tree(base);
            break;
        }
        case SSA_NAME: {
	        if (SSA_NAME_IDENTIFIER(node))
                print_tree(SSA_NAME_IDENTIFIER (node));
            printf("_%d", SSA_NAME_VERSION(node));
            break;
        }
        default:
            break;

    }
    // For type of tree //
    /* switch (TREE_CODE(TREE_TYPE(node))) {
        case INTEGER_TYPE:
            printf("- integer\n");
            break;
        case VOID_TYPE:
            printf("- void\n");
            break;
        case REAL_TYPE:
            printf("- real\n");
            break;
        case BOOLEAN_TYPE:
            printf("- bool\n");
            break;
        default:
            printf("- another\n");
            break;
    } */
}

const char* getSymbol(enum tree_code code) {
    switch (code) {
        case TRUTH_OR_EXPR:     return "||";
        case TRUTH_ORIF_EXPR:   return "||";
        case TRUTH_AND_EXPR:    return "&&";
        case TRUTH_ANDIF_EXPR:  return "&&";
        case EQ_EXPR:           return "==";
        case NE_EXPR:           return "!=";
        case LT_EXPR:           return "<";
        case LE_EXPR:           return "<=";
        case GT_EXPR:           return ">";
        case GE_EXPR:           return ">=";
        case PLUS_EXPR:         return "+";
        case MINUS_EXPR:        return "-";
        case MULT_EXPR:         return "*";
        case ROUND_MOD_EXPR:    return "%";
        case CEIL_MOD_EXPR:     return "%";
        case FLOOR_MOD_EXPR:    return "%";
        case TRUNC_MOD_EXPR:    return "%";
        default:                return " ";
    }
}

void print_bb_info() {
	basic_block bb;
  	gimple_stmt_iterator gsi;
  	gimple stmt;
    printf("===================================\n");
	printf("Info %s:\n",function_name(cfun));
    // printf("Info %s:\n",__PRETTY_FUNCTION__);
  	FOR_EACH_BB_FN (bb,cfun) {
        printf("Block index: %d\n", bb->index);
        edge e;
        edge_iterator ei;
        FOR_EACH_EDGE(e,ei,bb->preds) {
            printf("Index (edge_in): %d\n",e->src->index);
        }
        edge_iterator eo;
        FOR_EACH_EDGE(e,eo,bb->succs) {
            printf("Index (edge_out): %d\n",e->dest->index);
        }

      	/* Check all statements in the block.  */
        for (gsi = gsi_start_bb (bb); !gsi_end_p (gsi); gsi_next (&gsi)) {
	        stmt = gsi_stmt (gsi);
            switch (gimple_code(stmt)) {
                case GIMPLE_PHI: {
                    printf("PHI STMT := \n");
                    int params = gimple_phi_num_args(stmt);
                    int count=0;
                    for (count=0;count<params;count++) {
                        printf("arg%d: ",count);
                        tree param = gimple_phi_arg(stmt,count)->def;
                        print_tree(param);
                        printf("\n");
                    }
                    break;
                }
                case GIMPLE_COND: {
                    printf("COND STMT := \n");
                    tree lhs = gimple_cond_lhs(stmt);
                    tree rhs = gimple_cond_rhs(stmt);
                    print_tree(lhs);
                    printf(" %s ", getSymbol(gimple_cond_code(stmt)));
                    print_tree(rhs);
                    printf("\n");
                    break;
                }
                case GIMPLE_ASSIGN: {
                    printf("ASSIGN STMT := \n");
                    switch (gimple_num_ops(stmt)) {
			            case 0:
			            case 1: break;
                        case 2: {
                            tree lhs = gimple_assign_lhs(stmt);
                            tree rhs = gimple_assign_rhs1(stmt);
                            print_tree(lhs);
                            printf(" = ");
                            print_tree(rhs);
                            printf("\n");
                            break;
                        }
                        case 3: {
                            tree lhs = gimple_assign_lhs(stmt);
                            tree rhs1 = gimple_assign_rhs1(stmt);
                            tree rhs2 = gimple_assign_rhs2(stmt);
                            print_tree(lhs);
                            printf(" = ");
                            print_tree(rhs1);
                            // Return the code of the expression computed on the RHS of assignment statement G.
                            printf(" %s ", getSymbol(gimple_assign_rhs_code(stmt)));
                            print_tree(rhs2);
                            printf("\n");
                        }
                        default:
                            break;
                    }
                    break;
                }
                case GIMPLE_CALL: {
                    printf("CALL STMT := \n");
                    tree lhs = gimple_call_lhs (stmt);
                    if (lhs) {
                        print_tree(lhs);
                        printf(" = ");
                    }
                    printf(" %s\n",fndecl_name(gimple_call_fndecl(stmt)));
                    int params = gimple_call_num_args(stmt);
                    int count=0;
                    for (count=0;count<params;count++) {
                        printf("arg%d: ",count);
                        tree param = gimple_call_arg(stmt,count);
                        print_tree(param);
                        printf("\n");
                    }
                    break;
                }
                default: {
                    printf("ANOTHER STMT := \n");
                    print_gimple_stmt(stdout,stmt,0,TDF_SLIM);
                    break;
		        }
            }
	    }
    }
    printf("===================================\n");
}
