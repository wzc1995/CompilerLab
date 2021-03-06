//
// Generated by JTB 1.3.2
//

package spiglet.visitor;
import spiglet.spiglet2kanga.AllProc;
import spiglet.spiglet2kanga.PrintKanga;
import spiglet.spiglet2kanga.ProcInfo;
import spiglet.syntaxtree.*;

import java.util.Vector;

/**
 * Provides default methods which visit each node in the tree in depth-first
 * order.  Your visitors may extend this class.
 */
public class Spiglet2Kanga extends GJDepthFirst<String,String> {

    //从内存中找到该变量的值
    private String findTemp(String exchange, Integer curTempNum, ProcInfo curProc, String flag) {
        if(curProc.regT.containsKey(curTempNum)) return "t" + curProc.regT.get(curTempNum);
        if(curProc.regS.containsKey(curTempNum)) return "s" + curProc.regS.get(curTempNum);
        if(flag.equals("USE")) PrintKanga.println("ALOAD " + exchange + " SPILLEDARG " + curProc.stack.get(curTempNum));
        return exchange;
    }

    //把变量的值存回内存
    private void saveTemp(String exchange, Integer curTempNum, ProcInfo curProc) {
        if(curProc.regT.containsKey(curTempNum)) return;
        if(curProc.regS.containsKey(curTempNum)) return;
        PrintKanga.println("ASTORE SPILLEDARG " + curProc.stack.get(curTempNum) + " " + exchange);
    }

    //f0 -> ( ( Label() )? Stmt() )*的时候用，打印label

    public String visit(spiglet.syntaxtree.NodeOptional n, String argu) {
        if ( n.present() )
            PrintKanga.print(n.node.accept(this,argu));
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
        ProcInfo curProc = AllProc.get("MAIN");
        PrintKanga.pBegin("MAIN", curProc.argNum, curProc.stackUse, curProc.maxCallArg);

        n.f1.accept(this, "MAIN");
        PrintKanga.pEnd();
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

        ProcInfo curProc = AllProc.get(n.f0.f0.toString());
        PrintKanga.pBegin(n.f0.f0.toString(), curProc.argNum, curProc.stackUse, curProc.maxCallArg);

        n.f4.accept(this, n.f0.f0.toString());
        PrintKanga.pEnd();
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
        PrintKanga.println("NOOP");
        return null;
    }

    /**
     * f0 -> "ERROR"
     */
    public String visit(ErrorStmt n, String argu) {
        PrintKanga.println("ERROR");
        return null;
    }

    /**
     * f0 -> "CJUMP"
     * f1 -> Temp()
     * f2 -> Label()
     */
    public String visit(CJumpStmt n, String argu) {
        ProcInfo curProc = AllProc.get(argu);
        String tempName = n.f1.accept(this, argu);
        Integer curTempNum = Integer.parseInt(tempName.substring(5, tempName.length()));
        PrintKanga.println("CJUMP " + findTemp("v0", curTempNum, curProc, "USE") + " " + n.f2.f0.toString());
        return null;
    }

    /**
     * f0 -> "JUMP"
     * f1 -> Label()
     */
    public String visit(JumpStmt n, String argu) {
        PrintKanga.println("JUMP " + n.f1.f0.toString());
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
        String tempName0 = n.f1.accept(this, argu);
        String tempName1 = n.f3.accept(this, argu);

        Integer curTempNum0 = Integer.parseInt(tempName0.substring(5, tempName0.length()));
        Integer curTempNum1 = Integer.parseInt(tempName1.substring(5, tempName1.length()));
        PrintKanga.println("HSTORE " + findTemp("v0", curTempNum0, curProc, "USE") + " "
                + n.f2.f0.toString() + " " + findTemp("v1", curTempNum1, curProc, "USE"));
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
        String tempName0 = n.f1.accept(this, argu);
        String tempName1 = n.f2.accept(this, argu);

        Integer curTempNum0 = Integer.parseInt(tempName0.substring(5, tempName0.length()));
        Integer curTempNum1 = Integer.parseInt(tempName1.substring(5, tempName1.length()));

        PrintKanga.println("HLOAD " + findTemp("v0", curTempNum0, curProc, "DEF") + " "
                + findTemp("v1", curTempNum1, curProc, "USE") + " " + n.f3.f0.toString());
        saveTemp("v0", curTempNum0, curProc);
        return null;
    }

    /**
     * f0 -> "MOVE"
     * f1 -> Temp()
     * f2 -> Exp()
     */
    public String visit(MoveStmt n, String argu) {
        ProcInfo curProc = AllProc.get(argu);
        String tempName = n.f1.accept(this, argu);
        String argName = n.f2.accept(this, argu);
        Integer curTempNum = Integer.parseInt(tempName.substring(5, tempName.length()));

        if(argName.length() > 4 && argName.substring(0, 4).equals("TEMP")) {
            Integer curTempNum1 = Integer.parseInt(argName.substring(5, argName.length()));
            String firstTemp = findTemp("v0", curTempNum, curProc, "DEF");
            String secondTemp = findTemp("v1", curTempNum1, curProc, "USE");

            if(!firstTemp.equals(secondTemp)) {
                PrintKanga.println("MOVE " + firstTemp + " " + secondTemp);
                saveTemp("v0", curTempNum, curProc);
            }
        }
        else {
            PrintKanga.println("MOVE " + findTemp("v1", curTempNum, curProc, "DEF") + " " + argName);
            saveTemp("v1", curTempNum, curProc);
        }
        return null;
    }

    /**
     * f0 -> "PRINT"
     * f1 -> SimpleExp()
     */
    public String visit(PrintStmt n, String argu) {
        ProcInfo curProc = AllProc.get(argu);
        String argName = n.f1.accept(this, argu);
        if(argName.length() > 4 && argName.substring(0, 4).equals("TEMP")) {
            Integer curTempNum = Integer.parseInt(argName.substring(5, argName.length()));
            PrintKanga.println("PRINT " + findTemp("v1", curTempNum, curProc, "USE"));
        }
        else {
            PrintKanga.println("PRINT " + argName);
        }
        return null;
    }

    /**
     * f0 -> Call()
     *       | HAllocate()
     *       | BinOp()
     *       | SimpleExp()
     */
    public String visit(Exp n, String argu) {
        return n.f0.accept(this, argu);
    }

    /**
     * f0 -> "BEGIN"
     * f1 -> StmtList()
     * f2 -> "RETURN"
     * f3 -> SimpleExp()
     * f4 -> "END"
     */
    public String visit(StmtExp n, String argu) {

        //被调用者保存寄存器
        ProcInfo curProc = AllProc.get(argu);
        if(curProc.regSNum > 0) {
            int offset = Integer.max(curProc.argNum - 4, 0);
            for(int i = 0; i < curProc.regSNum; i++)
                PrintKanga.println("ASTORE SPILLEDARG " + (i + offset) + " s" + i);
        }

        //将参数提前放到Spiglet的寄存器对应的Kanga寄存器中
        if(curProc.argNum <= 4) {
            for (int i = 0; i < curProc.argNum; i++)
                if (curProc.tempSec.containsKey(i)) {
                    PrintKanga.println("MOVE " + findTemp("v1", i, curProc, "DEF") + " a" + i);
                    saveTemp("v1", i, curProc);
                }
        }
        else {
            for (int i = 0; i < 4; i++)
                if (curProc.tempSec.containsKey(i)) {
                    PrintKanga.println("MOVE " + findTemp("v1", i, curProc, "DEF") + " a" + i);
                    saveTemp("v1", i, curProc);
                }
            for (int i = 4; i < curProc.argNum; i++)
                if (curProc.tempSec.containsKey(i)) {
                    PrintKanga.println("ALOAD " + findTemp("v1", i, curProc, "DEF") + " SPILLEDARG " + (i - 4));
                    saveTemp("v1", i, curProc);
                }
        }

        n.f1.accept(this, argu);


        //传递返回值
        String argName = n.f3.accept(this, argu);
        if(argName.length() > 4 && argName.substring(0, 4).equals("TEMP")) {
            Integer curTempNum = Integer.parseInt(argName.substring(5, argName.length()));
            PrintKanga.println("MOVE v0 " + findTemp("v1", curTempNum, curProc, "USE"));
        }
        else {
            PrintKanga.println("MOVE v0 " + argName);
        }

        //被调用者恢复寄存器
        if(curProc.regSNum > 0) {
            int offset = Integer.max(curProc.argNum - 4, 0);
            for(int i = 0; i < curProc.regSNum; i++)
                PrintKanga.println("ALOAD " + "s" + i + " SPILLEDARG " + (i + offset));
        }
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

        String tempName0 = n.f1.accept(this, argu);
        Integer curTempNum0 = Integer.parseInt(tempName0.substring(5, tempName0.length()));

        //构造参数表
        Vector<Node> para = n.f3.nodes;
        if(para.size() <= 4) {
            for(int i = 0; i < para.size(); i++) {
                Integer curTempNum1 = Integer.parseInt(((Temp)para.get(i)).f1.f0.toString());
                PrintKanga.println("MOVE a" + i + " " + findTemp("v1", curTempNum1, curProc, "USE"));
            }
        }
        else {
            for(int i = 0; i < 4; i++) {
                Integer curTempNum1 = Integer.parseInt(((Temp)para.get(i)).f1.f0.toString());
                PrintKanga.println("MOVE a" + i + " " + findTemp("v1", curTempNum1, curProc, "USE"));
            }

            for(int i = 4; i < para.size(); i++) {
                Integer curTempNum1 = Integer.parseInt(((Temp)para.get(i)).f1.f0.toString());
                PrintKanga.println("PASSARG " + (i - 3) + " " + findTemp("v1", curTempNum1, curProc, "USE"));
            }
        }

        //构造CALL语句和返回值
        PrintKanga.println("CALL " + findTemp("v1", curTempNum0, curProc, "USE"));
        return "v0";
    }

    /**
     * f0 -> "HALLOCATE"
     * f1 -> SimpleExp()
     */
    public String visit(HAllocate n, String argu) {
        String _ret = "HALLOCATE ";

        ProcInfo curProc = AllProc.get(argu);
        String argName = n.f1.accept(this, argu);

        if(argName.length() > 4 && argName.substring(0, 4).equals("TEMP")) {
            Integer curTempNum = Integer.parseInt(argName.substring(5, argName.length()));
            _ret = _ret + findTemp("v1", curTempNum, curProc, "USE");
        }
        else {
            _ret = _ret + argName;
        }

        return _ret;
    }

    /**
     * f0 -> Operator()
     * f1 -> Temp()
     * f2 -> SimpleExp()
     */
    public String visit(BinOp n, String argu) {
        String _ret = n.f0.accept(this, argu) + " ";

        ProcInfo curProc = AllProc.get(argu);
        String tempName = n.f1.accept(this, argu);
        String argName = n.f2.accept(this, argu);
        Integer curTempNum = Integer.parseInt(tempName.substring(5, tempName.length()));

        if(argName.length() > 4 && argName.substring(0, 4).equals("TEMP")) {
            Integer curTempNum1 = Integer.parseInt(argName.substring(5, argName.length()));
            _ret = _ret + findTemp("v0", curTempNum, curProc, "USE") + " "
                    + findTemp("v1", curTempNum1, curProc, "USE");
        }
        else {
            _ret = _ret + findTemp("v1", curTempNum, curProc, "USE") + " " + argName;
        }

        return _ret;
    }

    /**
     * f0 -> "LT"
     *       | "PLUS"
     *       | "MINUS"
     *       | "TIMES"
     */
    public String visit(Operator n, String argu) {
        String[] _ret = { "LT", "PLUS", "MINUS", "TIMES" };
        return _ret[n.f0.which];
    }

    /**
     * f0 -> Temp()
     *       | IntegerLiteral()
     *       | Label()
     */
    public String visit(SimpleExp n, String argu) {
        return n.f0.accept(this, argu);
    }

    /**
     * f0 -> "TEMP"
     * f1 -> IntegerLiteral()
     */
    public String visit(Temp n, String argu) {
        return "TEMP " + n.f1.accept(this, argu);
    }

    /**
     * f0 -> <INTEGER_LITERAL>
     */
    public String visit(IntegerLiteral n, String argu) {
        return n.f0.toString();
    }

    /**
     * f0 -> <IDENTIFIER>
     */
    public String visit(Label n, String argu) {
        return n.f0.toString();
    }

}
