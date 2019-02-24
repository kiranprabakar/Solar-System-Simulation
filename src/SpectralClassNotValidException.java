/*
 * Thrown if user does not input a valid Spectral Class value
 */
public class SpectralClassNotValidException extends SolarSystemException{
    public SpectralClassNotValidException() {
        super();
    }

    public SpectralClassNotValidException(String message) {
        super(message);
    }
}
