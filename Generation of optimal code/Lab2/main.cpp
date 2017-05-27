#include "llvm/ADT/STLExtras.h"
#include "llvm/IR/IRBuilder.h"
#include "llvm/IR/LLVMContext.h"
#include "llvm/IR/Module.h"

#include <cstdio>
#include <cstdlib>
#include <string>
#include <map>
#include <vector>
#include <string>

enum Token {
  tok_eof = -1,

  // commands
  tok_if = -2,
  tok_then = -3,
  tok_else = -4,
  tok_for = -5,
  tok_in = -6,
  

  // primary
  tok_identifier = -7,
  tok_number = -8

};

static std::string IdentifierStr; 
static int NumVal;

static int gettok() {
    static int LastChar = ' ';
    while (isspace(LastChar))
        LastChar = getchar();

    if (isalpha(LastChar)) {
        IdentifierStr = LastChar;
        while (isalnum((LastChar = getchar())))
            IdentifierStr += LastChar;

        if (IdentifierStr == "if") return tok_if;
        if (IdentifierStr == "then") return tok_then;
        if (IdentifierStr == "else") return tok_else;
        if (IdentifierStr == "for") return tok_for;
        if (IdentifierStr == "in") return tok_in;
        return tok_identifier;
    }

    if (isdigit(LastChar)) {
        std::string NumStr;
        do {
            NumStr += LastChar;
            LastChar = getchar();
        } while (isdigit(LastChar));

        NumVal = strtod(NumStr.c_str(), 0);
        return tok_number;
    }

    if (LastChar == EOF)
        return tok_eof;

    int ThisChar = LastChar;
    LastChar = getchar();
    return ThisChar;
}

class ExprAST {
    public:
        virtual ~ExprAST() {}
        virtual llvm::Value *codegen() = 0;
};

class NumberExprAST: public ExprAST {
    int Val;
 
    public:
        NumberExprAST(int Val) : Val(Val) {}
        llvm::Value *codegen() override;
};

class VariableExprAST : public ExprAST {
    std::string varName;

    public:
        VariableExprAST(const std::string &varName) : varName(varName) {}
        llvm::Value *codegen() override;
};

class AssignExprAST : public ExprAST {
    std::string varName;
    ExprAST *Expr;
    public:
        AssignExprAST(const std::string &varName, ExprAST *Expr): varName(varName),Expr(Expr) {}
        llvm::Value *codegen() override;
};

class IfExprAST: public ExprAST {
    ExprAST *Cond, *Then, *Else;
    public:
        IfExprAST(ExprAST *Cond, ExprAST *Then, ExprAST *Else): Cond(Cond), Then(Then), Else(Else)  {}   
        llvm::Value *codegen() override;
};

class ForExprAST: public ExprAST {
    std::string varName;
    ExprAST *Start, *End, *Step, *Body;
    public:
        ForExprAST(std::string varName, ExprAST *Start, ExprAST *End, ExprAST *Step, ExprAST *Body): varName(varName),Start(Start), End(End), Step(Step), Body(Body) {}   
        llvm::Value *codegen() override;
};

class BinaryExprAST: public ExprAST {
    char Op;
    ExprAST *Lhs, *Rhs;
 
    public:
        BinaryExprAST(char Op, ExprAST *Lhs, ExprAST *Rhs): Op(Op), Lhs(Lhs), Rhs(Rhs) {}
        llvm::Value *codegen() override;
};

static int CurTok;
static int getNextToken() { 
    return CurTok = gettok();
}

void LogError(const char *Str) {
    fprintf(stderr, "Error: %s\n", Str);
}

static ExprAST *ParseExpression();

static ExprAST *ParseNumberExpr() {
    auto Result = new NumberExprAST(NumVal);
    getNextToken();
    return Result;
}

static ExprAST *ParseParenExpr() {
    getNextToken();
    auto V = ParseExpression();
    if (!V)
        return nullptr;

    if (CurTok != ')') {
        LogError("expected ')'");
        return nullptr;
    }
    getNextToken();
    return V;
}


static ExprAST* ParseVariableExpr() {
    std::string varName = IdentifierStr;

    getNextToken();

    if (CurTok != '=')
        return new VariableExprAST(varName);

    getNextToken();
    auto varDecl = ParseExpression();
    if (!varDecl) {
        LogError("expected '='");
        return nullptr;
    }
    return new AssignExprAST(varName,varDecl);
}

static ExprAST* ParseIfExpr() {
    getNextToken();

    auto Cond = ParseExpression();
    if (!Cond)
        return nullptr;

    if (CurTok != tok_then) {
        LogError("expected 'then'");
        return nullptr;
    }
    
    getNextToken();

    auto Then = ParseExpression();
    if (!Then)
        return nullptr;

    if (CurTok != tok_else) {
        LogError("expected 'else'");
        return nullptr;
    }

    getNextToken();

    auto Else = ParseExpression();
    if (!Else)
        return nullptr;

    return new IfExprAST(Cond,Then,Else);
}

static ExprAST* ParseForExpr() {
    getNextToken();

    if (CurTok != tok_identifier) {
        LogError("expected variable after keyword 'for'");
        return nullptr;
    }

    std::string IdName = IdentifierStr;
    getNextToken();

    if (CurTok != '=') {
        LogError("expected '=' after for");
        return nullptr;
    }
    
    getNextToken(); 
    auto Start = ParseExpression();
    if (!Start)
        return nullptr;
    if (CurTok != ',') {
        LogError("expected ',' after for start value");
        return nullptr;
    }
  
    getNextToken();

    auto End = ParseExpression();
    if (!End)
        return nullptr;

    ExprAST* Step = nullptr;
    if (CurTok == ';') {
        getNextToken();
        Step = ParseExpression();
        if (!Step)
            return nullptr;
    }

    if (CurTok != tok_in) {
        LogError("expected 'in' after for");
        return nullptr;
    }
  
    getNextToken();

    auto Body = ParseExpression();
    if (!Body)
        return nullptr;

    return new ForExprAST(IdName,Start,End,Step,Body);
}


static ExprAST* ParsePrimary() {
    switch (CurTok) {
        case tok_identifier:
            return ParseVariableExpr();
        case tok_number:
            return ParseNumberExpr();
        case '(':
            return ParseParenExpr();
        case tok_if:
            return ParseIfExpr();
        case tok_for:
            return ParseForExpr();
        default: {
            LogError("unknown token when expecting an expression");
            return nullptr;
        }
    }
}


static ExprAST* ParseBinary(ExprAST* LHS) {
    while (true) {
        if ((CurTok != '-') && (CurTok != '+')) 
            return LHS;
        char Op = CurTok;
        getNextToken(); 

        auto RHS = ParsePrimary();
        if (!RHS)
            return nullptr;

        LHS = new BinaryExprAST(Op, LHS, RHS);
    }
}

static ExprAST* ParseExpression() {
    auto LHS = ParsePrimary();
    if (!LHS)
        return nullptr;
    return ParseBinary(LHS);
}

static llvm::LLVMContext TheContext;
static llvm::IRBuilder<> Builder(llvm::getGlobalContext());
static llvm::Module *TheModule;
static llvm::Function *MainFunction;
static std::map<std::string, llvm::Value*> NamedValues;

llvm::Value *NumberExprAST::codegen() {
    return llvm::ConstantInt::get(llvm::getGlobalContext(), llvm::APInt(32,Val,false));
}

llvm::Value *VariableExprAST::codegen() {
    llvm::Value *V = NamedValues[varName];
    if (!V) {
        LogError("Unknown variable name");
        return nullptr;
    }
    return V;
}

llvm::Value *BinaryExprAST::codegen() {
    llvm::Value *L = Lhs->codegen();
    llvm::Value *R = Rhs->codegen();
    if (!L || !R)
        return nullptr;

    switch (Op) {
        case '+':
            return Builder.CreateAdd(L, R, "addtmp");
        case '-':
            return Builder.CreateSub(L, R, "subtmp");
        default:
            LogError("invalid binary operator");
            return nullptr;
    }
}

llvm::Value *AssignExprAST::codegen(){
    llvm::Value *v = Expr->codegen();
    if (!v)
        return nullptr;

    llvm::AllocaInst *Alloca = Builder.CreateAlloca(llvm::Type::getInt32Ty(llvm::getGlobalContext()), 0, varName.c_str());
    Builder.CreateStore(v, Alloca);
    llvm::Value *CurVar = Builder.CreateLoad(Alloca);

    NamedValues[varName] = Alloca;
    return CurVar;
}

llvm::Value *IfExprAST::codegen() {
    llvm::Value *CondV = Cond->codegen();
    if (!CondV)
        return nullptr;

    CondV = Builder.CreateICmpNE(
            CondV, llvm::ConstantInt::get(llvm::getGlobalContext(), llvm::APInt(32,0,false)), "ifcond");

    MainFunction = Builder.GetInsertBlock()->getParent();

    // Создаём блоки для веток then и else.  Вставляем блок 'then' в
    // конец функции.
    llvm::BasicBlock *ThenBB = llvm::BasicBlock::Create(TheContext, "then", MainFunction);
    llvm::BasicBlock *ElseBB = llvm::BasicBlock::Create(TheContext, "else");
    llvm::BasicBlock *MergeBB = llvm::BasicBlock::Create(TheContext, "ifcont");

    Builder.CreateCondBr(CondV, ThenBB, ElseBB);

    // Генерируем значение.
    Builder.SetInsertPoint(ThenBB);

    llvm::Value *ThenV = Then->codegen();
    if (!ThenV)
        return nullptr;

    Builder.CreateBr(MergeBB);
    // Кодогенерация 'Then' может изменить текущий блок, обновляем ThenBB для PHI.
    ThenBB = Builder.GetInsertBlock();

    /// Генерируем блок else.
    MainFunction->getBasicBlockList().push_back(ElseBB);
    Builder.SetInsertPoint(ElseBB);

    llvm::Value *ElseV = Else->codegen();
    if (!ElseV)
        return nullptr;

    Builder.CreateBr(MergeBB);
    // Кодогенерация 'Else' может изменить текущий блок, обновляем ElseBB для PHI.
    ElseBB = Builder.GetInsertBlock();

    // Генерация блока слияния.
    MainFunction->getBasicBlockList().push_back(MergeBB);
    Builder.SetInsertPoint(MergeBB);
    llvm::PHINode *PN = Builder.CreatePHI(llvm::Type::getInt32Ty(llvm::getGlobalContext()), 2, "iftmp");

    PN->addIncoming(ThenV, ThenBB);
    PN->addIncoming(ElseV, ElseBB);
    return PN;
}


llvm::Value *ForExprAST::codegen() {
    // Emit the start code first, without 'variable' in scope.
    llvm::Value *StartVal = Start->codegen();
    if (!StartVal)
        return nullptr;

    // Make the new basic block for the loop header, inserting after current
    // block.
    MainFunction = Builder.GetInsertBlock()->getParent();
    llvm::BasicBlock *PreheaderBB = Builder.GetInsertBlock();
    llvm::BasicBlock *LoopBB = llvm::BasicBlock::Create(llvm::getGlobalContext(), "loop", MainFunction);

    // Insert an explicit fall through from the current block to the LoopBB.
    Builder.CreateBr(LoopBB);

    // Start insertion in LoopBB.
    Builder.SetInsertPoint(LoopBB);

    // Start the PHI node with an entry for Start.
    llvm::PHINode *Variable = Builder.CreatePHI(llvm::Type::getInt32Ty(llvm::getGlobalContext()), 2, varName);
    Variable->addIncoming(StartVal, PreheaderBB);

    // Within the loop, the variable is defined equal to the PHI node.  If it
    // shadows an existing variable, we have to restore it, so save it now.
    llvm::Value *OldVal = NamedValues[varName];
    NamedValues[varName] = Variable;

    // Emit the body of the loop.  This, like any other expr, can change the
    // current BB.  Note that we ignore the value computed by the body, but don't
    // allow an error.
    if (!Body->codegen())
        return nullptr;

    // Emit the step value.
    llvm::Value *StepVal = nullptr;
    if (Step) {
        StepVal = Step->codegen();
        if (!StepVal)
            return nullptr;
    } else {
        // If not specified, use 1.0.
        StepVal = llvm::ConstantInt::get(llvm::getGlobalContext(), llvm::APInt(32,1,false));
    }

    llvm::Value *NextVar = Builder.CreateAdd(Variable, StepVal, "nextvar");

    // Compute the end condition.
    llvm::Value *EndCond = End->codegen();
    if (!EndCond)
        return nullptr;

    // Convert condition to a bool by comparing non-equal to 0.
    EndCond = Builder.CreateICmpNE(
            EndCond, llvm::ConstantInt::get(llvm::getGlobalContext(), llvm::APInt(32,0,false)), "loopcond");

    // Create the "after loop" block and insert it.
    llvm::BasicBlock *LoopEndBB = Builder.GetInsertBlock();
    llvm::BasicBlock *AfterBB =
            llvm::BasicBlock::Create(llvm::getGlobalContext(), "afterloop", MainFunction);

    // Insert the conditional branch into the end of LoopEndBB.
    Builder.CreateCondBr(EndCond, LoopBB, AfterBB);

    // Any new code will be inserted in AfterBB.
    Builder.SetInsertPoint(AfterBB);

    // Add a new entry to the PHI node for the backedge.
    Variable->addIncoming(NextVar, LoopEndBB);

    // Restore the unshadowed variable.
    if (OldVal)
        NamedValues[varName] = OldVal;
    else
        NamedValues.erase(varName);

    // for expr always returns 0.
    return llvm::Constant::getNullValue(llvm::Type::getInt32Ty(TheContext));
}

static llvm::Value *Parse() {
    getNextToken();

    ExprAST *expr = nullptr;
    llvm::Value *RetVal = nullptr;
    while (CurTok != tok_eof) {
        expr = ParseExpression();
        if (expr) {
            RetVal = expr->codegen();
            if (!RetVal) getNextToken();
        } else {
            return nullptr;
        }
    }

    return RetVal;
}


int main() {
    // Создаём модуль, который будет хранить весь код.
    TheModule = new llvm::Module("main", TheContext);

    MainFunction = TheModule->getFunction("main");

    llvm::FunctionType *FT =
            llvm::FunctionType::get(Builder.getInt32Ty(),false);
    MainFunction =
            llvm::Function::Create(FT, llvm::GlobalValue::CommonLinkage, "main", TheModule);

    llvm::BasicBlock *BB = llvm::BasicBlock::Create(TheContext, "entrypoint", MainFunction);
    Builder.SetInsertPoint(BB);

    llvm::Value *ret = Parse();
    Builder.CreateRet(ret);


    // Выводим сгенерированный код.
    TheModule->dump();

    return 0;
}
