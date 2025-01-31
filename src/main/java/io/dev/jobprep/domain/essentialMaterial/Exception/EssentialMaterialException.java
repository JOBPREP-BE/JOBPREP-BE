package io.dev.jobprep.domain.essentialMaterial.Exception;
import io.dev.jobprep.exception.code.ErrorCode;
import io.dev.jobprep.exception.exception_class.CustomException;

public class EssentialMaterialException extends CustomException {
    public EssentialMaterialException(ErrorCode errorCode) {
        super(errorCode);
    }
}

