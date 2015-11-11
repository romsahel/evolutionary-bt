package pacman.entries.pacman.behaviortree;

import pacman.entries.pacman.behaviortree.helpers.Composite;
import pacman.entries.pacman.behaviortree.helpers.Node;
import java.util.Random;

public class Mutator {
    private int numOfLeafs = 0;
    private int numOfComposites = 0;
    private int cur = 0;
    Random random = new Random();

    public void Mutate(Composite root){
        if(random.nextBoolean()){
            MutateLeaf(root);
        }
        else{
            MutateComposite(root);
        }
    }

    private void MutateLeaf(Composite root){
        GetNumOfLeafs(root);
        int index = random.nextInt(numOfLeafs);
        SelectLeaf(root, index);
    }

    private void SelectLeaf(Composite root, int index){
        for (int i = 0; i < root.getChildrenCount(); i++){
            if(root.getChildren().get(i) instanceof Composite){
                Composite composite = (Composite)root.getChildren().get(i);
                SelectLeaf(composite, index);
            }
            else{
                if(cur == index){
                    if(i == root.getChildrenCount()-1){
                        //node equals new action
                    }
                    else{
                        if(random.nextBoolean()){
                            //node equals new condition
                        }
                        else{
                            //remove condition
                        }
                    }
                }
                else{
                    cur++;
                }
            }
        }
    }

    private void GetNumOfLeafs(Composite root){
        for (Node node : root.getChildren()){
            if(node instanceof Composite){
                Composite composite = (Composite)node;
                GetNumOfLeafs(composite);
            }
            else{
                numOfLeafs++;
            }
        }
    }

    private void MutateComposite(Composite root){
        GetNumOfComposites(root);
        int index = random.nextInt(numOfComposites);
        SelectComposite(root, index);
    }

    private void SelectComposite(Composite root, int index){
        for (int i = 0; i < root.getChildrenCount(); i++){
            if(root.getChildren().get(i) instanceof Composite){
                if(cur == index){
                    //delete composite
                }
                else{
                    cur++;
                    Composite composite = (Composite)root.getChildren().get(i);
                    SelectComposite(composite, index);
                }

            }
        }
    }

    private void GetNumOfComposites(Composite root){
        for (Node node : root.getChildren()){
            if(node instanceof Composite){
                numOfComposites++;
                Composite composite = (Composite)node;
                GetNumOfComposites(composite);
            }
        }
    }
}
