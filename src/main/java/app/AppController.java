package app;

//comment
//extra
public class AppController {
    private final AppModel model;
    //private final AppModel model2;
    private final AppView view;

    public AppController(AppModel model, AppView view) {
        this.model = model;
        //this.model2 = model2;
        this.view = view;

        //view.setMessage(model2.getMessage());
        
        view.setMessage(model.getMessage());
        view.setOnGreeting(this::greet);
        view.setOnGreeting2(this::greet2);
    }

    private void greet() {
        model.setMessage("Hallo!");
        view.setMessage(model.getMessage());
    }
    private void greet2() {
        model.setMessage("KUCHEN");
        view.setMessage(model.getMessage());
    }
}
