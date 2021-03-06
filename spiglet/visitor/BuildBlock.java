//
// Generated by JTB 1.3.2
//

package spiglet.visitor;
import spiglet.spiglet2kanga.AllProc;
import spiglet.spiglet2kanga.ProcInfo;
import spiglet.syntaxtree.*;

/**
 * 建立基本块，为了方便，一条一句一个基本块
 * 对每个块求左右集合，统计每个过程的语句个数并记录Call语句的位置
 * 记录每个过程的参数个数
 */

public class BuildBlock extends GJDepthFirst<String, String> {


    //label()?使用
    public String visit(NodeOptional n, String argu) {
        if ( n.present() ) {
            ProcInfo curProc = AllProc.get(argu);
            curProc.labelInfo.put(n.node.accept(this,argu), curProc.stmtNum);
            return n.node.accept(this,argu);
        }
        else
            return null;
    }

    //
    // User-generated visitor methods below
    //

    /**
     * f0 -> "MAIN"
     * f1 -> StmtList()
     * f2 -> "END"
     * f3 -> ( Procedure() )*
     * f4 -> <EOF>
     */
    public String visit(Goal n, String argu) {
        ProcInfo curProc = new ProcInfo();
        curProc.argNum = 0;
        curProc.stackUse = 0;
        AllProc.insert("MAIN", curProc);

        //MAIN stmt
        curProc.graph.insertNode(curProc.stmtNum);
        curProc.stmtNum++;

        n.f1.accept(this, "MAIN");

        //END stmt
        curProc.graph.insertNode(curProc.stmtNum);
        curProc.stmtNum++;

        n.f3.accept(this, argu);

        return null;
    }

    /**
     * f0 -> ( ( Label() )? Stmt() )*
     */
    public String visit(StmtList n, String argu) {
        n.f0.accept(this, argu);
        return null;
    }

    /**
     * f0 -> Label()
     * f1 -> "["
     * f2 -> IntegerLiteral()
     * f3 -> "]"
     * f4 -> StmtExp()
     */
    public String visit(Procedure n, String argu) {
        ProcInfo curProc = new ProcInfo();
        curProc.argNum = Integer.parseInt(n.f2.f0.toString());
        curProc.stackUse = Integer.max(0, curProc.argNum - 4);
        AllProc.insert(n.f0.f0.toString(), curProc);

        n.f4.accept(this, n.f0.f0.toString());
        return null;
    }

    /**
     * f0 -> NoOpStmt()
     *       | ErrorStmt()
     *       | CJumpStmt()
     *       | JumpStmt()
     *       | HStoreStmt()
     *       | HLoadStmt()
     *       | MoveStmt()
     *       | PrintStmt()
     */
    public String visit(Stmt n, String argu) {
        n.f0.accept(this, argu);
        return null;
    }

    /**
     * f0 -> "NOOP"
     */
    public String visit(NoOpStmt n, String argu) {
        ProcInfo curProc = AllProc.get(argu);
        curProc.graph.insertNode(curProc.stmtNum);
        curProc.stmtNum++;

        n.f0.accept(this, argu);
        return null;
    }

    /**
     * f0 -> "ERROR"
     */
    public String visit(ErrorStmt n, String argu) {
        ProcInfo curProc = AllProc.get(argu);
        curProc.graph.insertNode(curProc.stmtNum);
        curProc.stmtNum++;

        n.f0.accept(this, argu);
        return null;
    }

    /**
     * f0 -> "CJUMP"
     * f1 -> Temp()
     * f2 -> Label()
     */
    public String visit(CJumpStmt n, String argu) {
        n.f0.accept(this, argu);


        ProcInfo curProc = AllProc.get(argu);
        curProc.graph.insertNode(curProc.stmtNum);
        curProc.graph.insertUse(curProc.stmtNum, Integer.parseInt(n.f1.f1.f0.toString()));
        curProc.stmtNum++;

        return null;
    }

    /**
     * f0 -> "JUMP"
     * f1 -> Label()
     */
    public String visit(JumpStmt n, String argu) {

        ProcInfo curProc = AllProc.get(argu);
        curProc.graph.insertNode(curProc.stmtNum);
        curProc.stmtNum++;

        return null;
    }

    /**
     * f0 -> "HSTORE"
     * f1 -> Temp()
     * f2 -> IntegerLiteral()
     * f3 -> Temp()
     */
    public String visit(HStoreStmt n, String argu) {

        ProcInfo curProc = AllProc.get(argu);
        curProc.graph.insertNode(curProc.stmtNum);
        curProc.graph.insertUse(curProc.stmtNum, Integer.parseInt(n.f1.f1.f0.toString()));
        curProc.graph.insertUse(curProc.stmtNum, Integer.parseInt(n.f3.f1.f0.toString()));
        curProc.stmtNum++;

        return null;
    }

    /**
     * f0 -> "HLOAD"
     * f1 -> Temp()
     * f2 -> Temp()
     * f3 -> IntegerLiteral()
     */
    public String visit(HLoadStmt n, String argu) {

        ProcInfo curProc = AllProc.get(argu);
        curProc.graph.insertNode(curProc.stmtNum);
        curProc.graph.insertDef(curProc.stmtNum, Integer.parseInt(n.f1.f1.f0.toString()));
        curProc.graph.insertUse(curProc.stmtNum, Integer.parseInt(n.f2.f1.f0.toString()));
        curProc.stmtNum++;

        return null;
    }

    /**
     * f0 -> "MOVE"
     * f1 -> Temp()
     * f2 -> Exp()
     */
    public String visit(MoveStmt n, String argu) {


        ProcInfo curProc = AllProc.get(argu);
        curProc.graph.insertNode(curProc.stmtNum);
        curProc.graph.insertDef(curProc.stmtNum, Integer.parseInt(n.f1.f1.f0.toString()));

        n.f2.accept(this, argu);

        curProc.stmtNum++;
        return null;
    }

    /**
     * f0 -> "PRINT"
     * f1 -> SimpleExp()
     */
    public String visit(PrintStmt n, String argu) {
        ProcInfo curProc = AllProc.get(argu);
        curProc.graph.insertNode(curProc.stmtNum);

        n.f1.accept(this, argu);

        curProc.stmtNum++;
        return null;
    }

    /**
     * f0 -> Call()
     *       | HAllocate()
     *       | BinOp()
     *       | SimpleExp()
     */
    public String visit(Exp n, String argu) {
        n.f0.accept(this, argu);
        return null;
    }

    /**
     * f0 -> "BEGIN"
     * f1 -> StmtList()
     * f2 -> "RETURN"
     * f3 -> SimpleExp()
     * f4 -> "END"
     */
    public String visit(StmtExp n, String argu) {
        //BEGIN stmt
        ProcInfo curProc = AllProc.get(argu);
        curProc.graph.insertNode(curProc.stmtNum);
        curProc.stmtNum++;

        n.f1.accept(this, argu);

        //RETURN ___ stmt
        curProc.graph.insertNode(curProc.stmtNum);
        n.f3.accept(this, argu);
        curProc.stmtNum++;

        //END stmt
        curProc.graph.insertNode(curProc.stmtNum);
        curProc.stmtNum++;
        return null;
    }

    /**
     * f0 -> "CALL"
     * f1 -> SimpleExp()
     * f2 -> "("
     * f3 -> ( Temp() )*
     * f4 -> ")"
     */
    public String visit(Call n, String argu) {

        ProcInfo curProc = AllProc.get(argu);
        curProc.callStmt.add(curProc.stmtNum);

        n.f1.accept(this, argu);
        n.f3.accept(this, argu);

        curProc.maxCallArg = Integer.max(curProc.maxCallArg, n.f3.size());
        return null;
    }

    /**
     * f0 -> "HALLOCATE"
     * f1 -> SimpleExp()
     */
    public String visit(HAllocate n, String argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        return null;
    }

    /**
     * f0 -> Operator()
     * f1 -> Temp()
     * f2 -> SimpleExp()
     */
    public String visit(BinOp n, String argu) {
        ProcInfo curProc = AllProc.get(argu);
        curProc.graph.insertUse(curProc.stmtNum, Integer.parseInt(n.f1.f1.f0.toString()));
        n.f2.accept(this, argu);
        return null;
    }

    /**
     * f0 -> "LT"
     *       | "PLUS"
     *       | "MINUS"
     *       | "TIMES"
     */
    public String visit(Operator n, String argu) {
        n.f0.accept(this, argu);
        return null;
    }

    /**
     * f0 -> Temp()
     *       | IntegerLiteral()
     *       | Label()
     */
    public String visit(SimpleExp n, String argu) {
        n.f0.accept(this, argu);
        return null;
    }

    /**
     * f0 -> "TEMP"
     * f1 -> IntegerLiteral()
     */
    //参数表或者SimpleExp的TEMP会到这里来，需要放到当前语句块的Right集合中，（其余TEMP入口已被封闭）。
    public String visit(Temp n, String argu) {
        ProcInfo curProc = AllProc.get(argu);
        curProc.graph.insertUse(curProc.stmtNum, Integer.parseInt(n.f1.f0.toString()));
        return null;
    }

    /**
     * f0 -> <INTEGER_LITERAL>
     */
    public String visit(IntegerLiteral n, String argu) {
        n.f0.accept(this, argu);
        return null;
    }

    /**
     * f0 -> <IDENTIFIER>
     */
    //给Option里的Label返回字符串
    public String visit(Label n, String argu) {
        return n.f0.toString();
    }

}
