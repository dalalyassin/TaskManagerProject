package MainPackageForTaskManager.Exception;

public class NotAllowedException extends  RuntimeException{

    public NotAllowedException(String message){
        super(message);
    }
}