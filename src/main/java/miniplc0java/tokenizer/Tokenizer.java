package miniplc0java.tokenizer;

import miniplc0java.error.TokenizeError;
import miniplc0java.error.ErrorCode;

import miniplc0java.util.Pos;

public class Tokenizer {

    private StringIter it;

    public Tokenizer(StringIter it) {
        this.it = it;
    }

    // 这里本来是想实现 Iterator<Token> 的，但是 Iterator 不允许抛异常，于是就这样了

    /**
     * 获取下一个 Token
     *
     * @return
     * @throws TokenizeError 如果解析有异常则抛出
     */
    public Token nextToken() throws TokenizeError {
        it.readAll();

        // 跳过之前的所有空白字符
        skipSpaceCharacters();

        if (it.isEOF()) {
            return new Token(TokenType.EOF, "", it.currentPos(), it.currentPos());
        }

        char peek = it.peekChar();
        if (Character.isDigit(peek)) {
            return lexUInt();
        } else if (Character.isAlphabetic(peek)) {
            return lexIdentOrKeyword();
        } else {
            return lexOperatorOrUnknown();
        }
    }

    private Token lexUInt() throws TokenizeError {
        // 请填空：
        // 直到查看下一个字符不是数字为止:
        // -- 前进一个字符，并存储这个字符
        //
        // 解析存储的字符串为无符号整数
        // 解析成功则返回无符号整数类型的token，否则返回编译错误
        //
        // Token 的 Value 应填写数字的值
        char peek = it.peekChar();
        int ret = 0;
        Pos prePos = it.currentPos();
        while (!it.isEOF() && Character.isDigit(peek)) {
            ret = ret * 10 + (peek - '0');
            it.nextChar();
        }
        return new Token(TokenType.Uint, ret, prePos, it.currentPos());
    }

    private Token lexIdentOrKeyword() throws TokenizeError {
        // 请填空：
        // 直到查看下一个字符不是数字或字母为止:
        // -- 前进一个字符，并存储这个字符
        //
        // 尝试将存储的字符串解释为关键字
        // -- 如果是关键字，则返回关键字类型的 token
        // -- 否则，返回标识符
        //
        // Token 的 Value 应填写标识符或关键字的字符串
        char peek = it.peekChar();
        Pos prePos = it.currentPos();
        TokenType type;
        StringBuilder ret = new StringBuilder("");
        while (!it.isEOF() && (Character.isAlphabetic(peek) || Character.isDigit(peek))) {
            ret.append(peek);
            it.nextChar();
        }
        switch (ret.toString()) {
            case "Begin":
                type = TokenType.Begin;
                break;
            case "End":
                type = TokenType.End;
                break;
            case "Var":
                type = TokenType.Var;
                break;
            case "Const":
                type = TokenType.Const;
                break;
            case "Print":
                type = TokenType.Print;
                break;
            default:
                type = TokenType.Ident;
                break;
        }
        return new Token(type, ret.toString(), prePos, it.currentPos());
    }

    private Token lexOperatorOrUnknown() throws TokenizeError {
        switch (it.nextChar()) {
            case '+':
                return new Token(TokenType.Plus, '+', it.previousPos(), it.currentPos());

            case '-':
                // 填入返回语句
                return new Token(TokenType.Minus, '-', it.previousPos(), it.currentPos());
            case '*':
                // 填入返回语句
                return new Token(TokenType.Mult, '*', it.previousPos(), it.currentPos());

            case '/':
                // 填入返回语句
                return new Token(TokenType.Div, '/', it.previousPos(), it.currentPos());
            // 填入更多状态和返回语句
            case '=':
                return new Token(TokenType.Equal, '=', it.previousPos(), it.currentPos());
            case ';':
                return new Token(TokenType.Semicolon, ';', it.previousPos(), it.currentPos());
            case '(':
                return new Token(TokenType.LParen, '(', it.previousPos(), it.currentPos());
            case ')':
                return new Token(TokenType.RParen, ')', it.previousPos(), it.currentPos());
            default:
                // 不认识这个输入，摸了
                throw new TokenizeError(ErrorCode.InvalidInput, it.previousPos());
        }
    }

    private void skipSpaceCharacters() {
        while (!it.isEOF() && Character.isWhitespace(it.peekChar())) {
            it.nextChar();
        }
    }
}
