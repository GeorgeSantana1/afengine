/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package afengine.part.scene;

import afengine.core.util.Vector;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * contains some actors<br>
 * use scene.updateScene to manager actor lifespan<br>
 * add sceneloader to scene,add actor to this Scene,<br>
 * add scene to SceneCenter,to manager scene lifespan<br>
 * @SceneCenter
 * @see Actor
 * @author Albert Flex
 */
public class Scene{
    //for simple use.
    private static class AdapterLoader extends AbSceneLoader{
        @Override
        public void load() {
            System.out.println("Load Scene:"+this.getThisScene().name);
        }
        @Override
        public void shutdown() {
            System.out.println("Shutdown Scene:"+this.getThisScene().name);
        }
        @Override
        public void pause() {
            System.out.println("Pause Scene:"+this.getThisScene().name);
        }
        @Override
        public void resume() {
            System.out.println("Resume Scene:"+this.getThisScene().name);            
        }        
    };
    
    
    private String name;
    public final Map<String,Actor> nodeActorMap=new HashMap<>();
    private AbSceneLoader loader;
    private final SceneCamera camera;
    private boolean shouldoutput;
    
    public Scene(){
        this("",new AdapterLoader());
    }

    public Scene(String name,AbSceneLoader loader){
        this.name=name;
        this.loader=loader;
        this.loader.setThisScene(this);
        shouldoutput=false;
        camera=new SceneCamera(new Vector(0,0,0,0),new Vector(0,0,0,0),0,0);
    }
    public Scene(String name){
        this(name,new AdapterLoader());
    }

    public AbSceneLoader getLoader() {
        return loader;
    }    

    public SceneCamera getCamera() {
        return camera;
    }
    
    public void setLoader(AbSceneLoader loader){
        this.loader=loader;
    }
    public final Map<String, Actor> getNodeActorMap() {
        return nodeActorMap;
    }
    
    public final Actor findActorByName(String name){

        Iterator<Actor> actoriter = Actor.staticActorMap.values().iterator();
        while(actoriter.hasNext()){
            Actor actor = actoriter.next();
            Actor dest = actor.findChild(name);
            if(dest!=null)
                return dest;
        }        
        
        actoriter = nodeActorMap.values().iterator();
        while(actoriter.hasNext()){
            Actor actor = actoriter.next();
            Actor dest = actor.findChild(name);
            if(dest!=null)
                return dest;
        }                
        
        return null;        
    }
    
    public final Actor findActorByID(long id){
        
        Iterator<Actor> actoriter = Actor.staticActorMap.values().iterator();
        while(actoriter.hasNext()){
            Actor actor = actoriter.next();
            Actor dest = findActor(id,actor);
            if(dest!=null)
                return dest;
        }        
        
        actoriter = nodeActorMap.values().iterator();
        while(actoriter.hasNext()){
            Actor actor = actoriter.next();
            Actor dest = findActor(id,actor);
            if(dest!=null)
                return dest;
        }                
        
        return null;
    }

    public boolean isShouldoutput() {
        return shouldoutput;
    }

    public void setShouldoutput(boolean shouldoutput) {
        this.shouldoutput = shouldoutput;
    }

    
    private Actor findActor(long id,Actor actor){
        if(actor.id==id)
            return actor;
        
        Deque<Actor> actordeque = new ArrayDeque<>();
        actordeque.addFirst(actor);
        Iterator<Actor> childiter;
        while(!actordeque.isEmpty()){
            Actor act = actordeque.pollLast();
            if(act.id==id)
                return act;

            childiter= act.getChildren().iterator();
            while(childiter.hasNext()){
                Actor acto = childiter.next();
                actordeque.addFirst(acto);
            }
        }
        
        return null;
    }
    
    public final void awakeAllActors(){
        Iterator<Actor> actoriter = Actor.staticActorMap.values().iterator();
        while(actoriter.hasNext()){
            Actor actor = actoriter.next();
            actor.awakeAllComponents();
        }        
        
        actoriter = nodeActorMap.values().iterator();
        while(actoriter.hasNext()){
            Actor actor = actoriter.next();
            if(!actor.isIsStatic())
                actor.awakeAllComponents();
        }                        
    }    
    
    public final void addNodeActor(String nodeName,Actor actor){
        nodeActorMap.put(nodeName, actor);
    }
    public final boolean hasNodeActor(String nodeName){
        return nodeActorMap.containsKey(nodeName);
    }
    public final void removeNodeActor(String nodeName){
        nodeActorMap.remove(nodeName);
    }        
    public String getName() {
        return name;
    }        
    public void setName(String name) {
        this.name = name;
    }       
    public void updateScene(long time){
        
        Iterator<Actor> actoriter = nodeActorMap.values().iterator();
        while(actoriter.hasNext()){
            Actor actor = actoriter.next();
            //如果静态实体中存在就不需要更新
            if(!actor.isIsStatic())
                actor.updateActor(time);
        }
    }
}
