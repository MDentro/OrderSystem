package nl.dentro.OrderSystem.dtos;

public class ImageUploadResponseDto {
    private String fileName;

    private String contentType;

    private String url;

    public ImageUploadResponseDto(String fileName, String contentType, String url) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.url = url;
    }

    public ImageUploadResponseDto() {
    }

    public String getFileName() {
        return fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public String getUrl() {
        return url;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
