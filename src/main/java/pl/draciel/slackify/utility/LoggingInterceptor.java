package pl.draciel.slackify.utility;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Aspect
@Component
public final class LoggingInterceptor {

    @Pointcut("execution(@pl.draciel.slackify.utility.LogIntercept * *(..))")
    public void annotatedMethod() {
    }

    @Around("annotatedMethod()")
    public Object around(final ProceedingJoinPoint pjp) throws Throwable {
        final CodeSignature signature = (CodeSignature) pjp.getSignature();
        final String signatureName = signature.getName();
        final String[] paramNames = signature.getParameterNames();
        final Object[] paramValues = pjp.getArgs();
        final String formattedArgs = formatSignature(paramNames, paramValues);

        final long startTime = System.nanoTime();
        beginExecution(signatureName, formattedArgs);
        final Object result = pjp.proceed();
        finishExecution(signatureName, formattedArgs, result, TimeUnit.NANOSECONDS.toMillis(System.nanoTime() -
                startTime));
        return result;
    }

    private static void beginExecution(final String signatureName, final String formattedArgs) {
        log.info("Started executing: {} ( {} ), Thread = {} ", signatureName, formattedArgs,
                Thread.currentThread().getName());
    }

    private static void finishExecution(final String signatureName, final String formattedArgs, final Object result,
                                        final long length) {
        log.info("Finished executing: {} ( {} ), Result = {}, Thread = {}, Time = {}", signatureName, formattedArgs,
                orNullString(result), Thread.currentThread().getName(), length);
    }

    private static String formatSignature(@Nonnull final String[] paramNames, @Nonnull final Object[] paramValues) {
        // TODO: make sure that this does not have memory impact or swap to string builder
        return IntStream.range(0, paramNames.length)
                .mapToObj(i -> formatParamAndValue(paramNames[i], paramValues[i]))
                .collect(Collectors.joining(", "));
    }

    private static String formatParamAndValue(@Nonnull final String paramName, @Nullable final Object paramValue) {
        return paramName + " = " + orNullString(paramValue);
    }

    private static String orNullString(@Nullable final Object value) {
        if (value == null) {
            return "null";
        }
        return value.toString();
    }
}
