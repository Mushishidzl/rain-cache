package com.mushi.raincache.key.gene;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 〈Spring EL 表达式解析〉
 *
 * @author mushi
 * @create 2018/12/6
 * @since 1.0.0
 */
public class SpringELParser {

    private static final Logger logger = LoggerFactory.getLogger(SpringELParser.class);

    private final ConcurrentHashMap<String, Expression> expCache = new ConcurrentHashMap<>();

    private final ExpressionParser parser = new SpelExpressionParser();


    private static final String ARGS = "args";

    private static final String RET_VAL = "ret_val";

    @SuppressWarnings("unchecked")
    public <T> T getELValue(String keySpEL, Object[] args, Class<T> classType, Object retVal, boolean isRet) {
        if (classType == String.class) {
            if (keySpEL.indexOf("#") == -1 && keySpEL.indexOf("'") == -1) {
                return (T) keySpEL; //如果不是EL表达式则返回原字符串
            }
        }

        StandardEvaluationContext context = new StandardEvaluationContext();

        context.setVariable(ARGS, args);
        if (isRet) {
            context.setVariable(RET_VAL, retVal);
        }

        Expression expression = expCache.get(keySpEL);

        if (expression == null) {
            expression = parser.parseExpression(keySpEL);
            expCache.put(keySpEL, expression);
        }

        return expression.getValue(context, classType);

    }

    public String getELStringValue(String keySpEL, Object[] args) {
        return getELValue(keySpEL, args, String.class, null, false);
    }

    public Boolean getELBooleanValue(String keySpEL, Object[] args) {
        return getELValue(keySpEL, args, Boolean.class, null, false);
    }

    public Boolean getELRetVal(String keySpEL, Object[] args, Object retVal) {
        return getELValue(keySpEL, args, Boolean.class, retVal, true);
    }


}