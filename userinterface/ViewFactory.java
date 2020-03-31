package userinterface;

import impresario.IModel;

//==============================================================================
public class ViewFactory {

    public static View createView(String viewName, IModel model) {
        if (viewName.equals("TLCView") == true) {
            return new TLCView(model);
        }
        else if(viewName.equals("AddScout") == true){
            return new AddScout(model);
        }
        else if(viewName.equals("SearchScout") == true){
            return new SearchScout(model);
        }
        else if( viewName.equals("ScoutCollectionView") == true){
            return new ScoutCollectionView(model);
        }
        else if(viewName.equals("EditScoutInfo") == true){
            return new EditScoutInfo(model);
        }
        else if(viewName.equals("AddTree") == true){
            return new AddTree(model);
        }
        else if(viewName.equals("SearchTree") == true){
            return new SearchTree(model);
        }
        else if(viewName.equals("TreeCollectionView") == true){
            return new TreeCollectionView(model);
        }
        else if(viewName.equals("EditTreeInfo") == true){
            return new EditTreeInfo(model);
        }
        else if(viewName.equals("AddTreeType")==true){
            return new AddTreeType(model);
        }
        else if(viewName.equals("SearchTreeType")== true){
            return new SearchTreeType(model);
        }
        else if(viewName.equals("EditTreeTypeInfo") == true){
            return new EditTreeTypeInfo(model);
        }
        else if(viewName.equals("TreeTypeCollectionView") == true){
            return new TreeTypeCollectionView(model);
        }
        else if(viewName.equals("OpenSession") == true){
            return new OpenSession(model);
        }
        else if(viewName.equals("OpenShift") == true){
            return new OpenShift(model);
        }


        else
            return null;
    }


}
