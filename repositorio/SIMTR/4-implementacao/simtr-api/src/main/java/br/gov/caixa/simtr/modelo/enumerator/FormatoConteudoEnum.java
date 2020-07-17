package br.gov.caixa.simtr.modelo.enumerator;

public enum FormatoConteudoEnum {

    BASE64("application/text"),
    BMP("image/bmp"),
    JPG("image/jpg"),
    JPEG("image/jpeg"),
    PDF("application/pdf"),
    PNG("image/png"),
    TIF("image/tif"),
    TIFF("image/tiff");

    private final String mimeType;

    private FormatoConteudoEnum(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }

    public static FormatoConteudoEnum getByMimeType(final String mimeType) {
        for (final FormatoConteudoEnum item : FormatoConteudoEnum.values()) {
            if (item.mimeType.equalsIgnoreCase(mimeType)) {
                return item;
            }
        }

        throw new IllegalArgumentException("Myme Type não conhecido: " + mimeType);
    }

    public static FormatoConteudoEnum getBySigla(final String sigla) {
        for (final FormatoConteudoEnum item : FormatoConteudoEnum.values()) {
            if (item.name().equalsIgnoreCase(sigla)) {
                return item;
            }
        }

        throw new IllegalArgumentException("Formato não conhecido: " + sigla);
    }
}
