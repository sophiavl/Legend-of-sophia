import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.collection.Array;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;

public class Game extends GameApplication {
    boolean coliding;

    private Entity player;
    private Entity forest;


    @Override
    protected void initGame() {
        player = entityBuilder()
                .type(EntityType.PLAYER)
                .at(300, 300)
//                .view(new Rectangle(25, 25, Color.BLUE))
                .viewWithBBox("tree.png")
                .with(new CollidableComponent(true))
                .buildAndAttach();

        forest = entityBuilder()
                .type(EntityType.FOREST)
                .at(500, 200)
//                .viewWithBBox(new Circle(15, 15, 15, Color.YELLOW))
                .viewWithBBox("forest1.png")
                .with(new CollidableComponent(true))
                .buildAndAttach();
    }


    public enum EntityType {
        PLAYER, FOREST
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("Basic Game App");
        settings.setVersion("0.1");
    }

    @Override
    protected void initInput() {

        onKey(KeyCode.D, () -> {
            player.translateX(5); // move right 5 pixels
            if (player.isColliding(forest)) {
                player.translateX(-5);
            }
            inc("pixelsMoved", +5);
        });

        onKey(KeyCode.A, () -> {
            player.translateX(-5); // move left 5 pixels
            inc("pixelsMoved", +5);
        });

        onKey(KeyCode.W, () -> {
            player.translateY(-5); // move up 5 pixels
            inc("pixelsMoved", +5);
        });

        onKey(KeyCode.S, () -> {
            player.translateY(5); // move down 5 pixels
            inc("pixelsMoved", +5);
        });
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("pixelsMoved", 0);
    }


    @Override
    protected void initPhysics() {
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.FOREST) {
            // order of types is the same as passed into the constructor
            @Override
            protected void onCollisionBegin(Entity player, Entity forest) {
                coliding = true;
            }

            protected void onCollisionEnd(Entity player, Entity forest) {
                coliding = false;
            }
        });
    }

    @Override
    protected void initUI() {
        Text textPixels = new Text();
        textPixels.setTranslateX(50); // x = 50
        textPixels.setTranslateY(100); // y = 100
        textPixels.textProperty().bind(getWorldProperties().intProperty("pixelsMoved").asString());

        var treeTexture = FXGL.getAssetLoader().loadTexture("forest.png", 400, 200);
        treeTexture.setTranslateX(-20);
        treeTexture.setTranslateY(-20);
        getGameScene().addUINode(treeTexture);


        getGameScene().addUINode(textPixels); // add to the scene graph
        getGameScene().setBackgroundRepeat("grassfield.png");
    }

    public static void main(String[] args) {
        launch(args);
    }
}