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


        else
            return null;
    }


}
