package bank.donghang.core.common.aop;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import bank.donghang.core.common.annotation.Mask;
import bank.donghang.core.common.annotation.MaskApply;
import bank.donghang.core.common.dto.MaskingDto;
import bank.donghang.core.common.dto.PageInfo;
import bank.donghang.core.common.enums.MaskingType;
import bank.donghang.core.common.util.MaskingUtil;

@Aspect
@EnableAspectJAutoProxy
@Component
public class MaskingAop {

	@Around("@annotation(bank.donghang.core.common.annotation.MaskApply)")
	public Object maskApplyAspect(
		final ProceedingJoinPoint joinPoint
	) throws Throwable {

		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		Method method = signature.getMethod();

		MaskApply maskApply = method.getAnnotation(MaskApply.class);

		Object[] args = joinPoint.getArgs();
		Object response = joinPoint.proceed();
		MaskingDto maskingOn = (MaskingDto)args[0];

		if (maskingOn.getDisableMasking()) {
			return response;
		} else {
			if (response != null) {
				return applyMasking(
					maskApply.typeValue(),
					maskApply.genericTypeValue(),
					response
				);
			}
		}

		return response;
	}

	private static <T> T applyMasking(
		Class<?> clazz,
		Class<?> klass,
		Object response
	)
		throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
		if(response instanceof PageInfo) {
			PageInfo<?> pageInfo = (PageInfo<?>) response;
			List<?> maskedList = applyMaskingForList(klass, pageInfo.getData());
			return (T) PageInfo.of(
				pageInfo.getPageToken(),
				maskedList,
				pageInfo.isHasNext()
			);
		} else if (response instanceof List) {
			return applyMaskingForList(klass, response);
		} else {
			return applyMaskingForRecord(clazz, response);
		}
	}

	private static <T> T applyMaskingForList(
		Class<?> klass,
		Object response
	)
		throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
		List<Object> responseDtoList = new ArrayList<>();
		List<?> responseList = (List<?>)response;
		for (Object responseDto : responseList) {
			if (responseDto != null && responseDto.getClass().equals(klass)) {
				Object maskedResponseDto = applyMaskingForRecord(
					klass,
					responseDto
				);
				responseDtoList.add(maskedResponseDto);
			} else {
				responseDtoList.add(responseDto);
			}
		}
		return (T)responseDtoList;
	}

	private static <T> T applyMaskingForRecord(
		Class<?> clazz,
		Object response
	) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
		Field[] fields = clazz.getDeclaredFields();
		List<Object> constructorArgs = new ArrayList<>();

		for (Field field : fields) {
			field.setAccessible(true);
			Object fieldValue = field.get(response);

			if (fieldValue instanceof String && field.isAnnotationPresent(Mask.class)) {
				Mask mask = field.getAnnotation(Mask.class);
				MaskingType maskingType = mask.type();
				fieldValue = MaskingUtil.maskingOf(
					maskingType,
					(String)fieldValue
				);
			}
			constructorArgs.add(fieldValue);
		}

		Constructor<?> constructor = clazz.getDeclaredConstructors()[0];
		return (T)constructor.newInstance(constructorArgs.toArray());
	}
}
