package kdrosado.trendyart.remote;

public class TokenServiceHolder {

    ArtsyApiInterface artsyApiInterface = null;

    public ArtsyApiInterface get() {
        return artsyApiInterface;
    }

    public void set(ArtsyApiInterface artsyApiInterface) {
        this.artsyApiInterface = artsyApiInterface;
    }
}
