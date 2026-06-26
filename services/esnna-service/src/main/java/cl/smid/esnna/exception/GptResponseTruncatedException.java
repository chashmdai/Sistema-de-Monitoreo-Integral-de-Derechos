package cl.smid.esnna.exception;

/**
 * IA-4b: finish_reason=length — la respuesta fue truncada por max_completion_tokens.
 * No es retryable (reintentar con el mismo presupuesto produce el mismo truncamiento)
 * y NO es una falla del proveedor: es un error de presupuesto propio. Se mapea a un
 * código distinto de MOTOR_IA_NO_DISPONIBLE para que el diagnóstico sea accionable
 * (subir esnna.openai.max-tokens-* tras revisar los reasoning_tokens registrados).
 */
public class GptResponseTruncatedException extends GptClientException {

    public GptResponseTruncatedException(String model, int maxTokens) {
        super("Respuesta truncada por límite de tokens (" + model + ", max_completion_tokens="
                + maxTokens + "); JSON incompleto. Revisar reasoning_tokens y aumentar presupuesto.",
                null, null, "length", false);
    }
}