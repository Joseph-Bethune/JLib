package jLibrary.nestedStringTree;

import jLibrary.JObject;
import jLibrary.JPrimitive;
import jLibrary.JString;
import jLibrary.interfaces.ConvertibleToJObject;
import jLibrary.miscFunctions.MiscFunctions;

import java.util.ArrayList;
import java.util.List;

import static jLibrary.miscFunctions.MiscFunctions.print;

public class AbstractNestedStringTree implements ConvertibleToJObject {

    public static class AbstractLeaf implements ConvertibleToJObject{
        protected long id;
        protected long parentLeafId;
        protected ArrayList<Long> childLeafIds;
        protected JPrimitive[] payloadStrings;

        // region constructors

        public AbstractLeaf(){
            clear();
        }

        public AbstractLeaf(AbstractLeaf original){
            setTo(original);
        }

        // endregion

        // region basic class functions

        public void clear(){
            id = -1;
            parentLeafId = -1;
            clearChildren();
            payloadStrings = null;
        }

        public void setTo(AbstractLeaf original){
            id = original.id;
            parentLeafId = original.parentLeafId;
            if (original.childLeafIds != null){
                this.childLeafIds.addAll(original.childLeafIds);
            }

            payloadStrings = new JPrimitive[original.payloadStrings.length];
            System.arraycopy(original.payloadStrings, 0, this.payloadStrings, 0, this.payloadStrings.length);
        }

        // endregion

        // region id

        public long getId(){
            return id;
        }

        public void setId(long id){
            this.id = id;
        }

        public String getIdString() {
            return constructIdString(this.id);
        }

        public static String constructIdString(long id){
            return getIdOpeningBracket() + id + getIdClosingBracket();
        }

        public static String getIdOpeningBracket(){
            return "<<|>>";
        }

        public static String getIdClosingBracket(){
            return "<<&>>";
        }

        // endregion

        // region payload strings

        public JPrimitive[] getPayloadSubstrings() {
            return payloadStrings;
        }

        public void replacePayloadSubstring(CharSequence valueToBeReplaced, CharSequence replacementValue){
            for(int x = 0; x < payloadStrings.length; x += 1){
                if(payloadStrings[x].equals(valueToBeReplaced)){
                    payloadStrings[x] = new JPrimitive(replacementValue);
                }
            }
        }

        // endregion

        // region children

        public ArrayList<Long> getChildLeafIds(){
            return childLeafIds;
        }

        public boolean hasChild(long childLeafId){
            return childLeafIds.contains(childLeafId);
        }

        public boolean hasChild(AbstractLeaf childLeaf){
            return hasChild(childLeaf.getId());
        }

        public void removeChild(long childLeafId){
            childLeafIds.remove((Long)childLeafId);
        }

        public void removeChild(AbstractLeaf childLeaf){
            removeChild(childLeaf.getId());
        }

        public void addChild(long newChildLeafId){
            if(!hasChild(newChildLeafId)){
                childLeafIds.add(newChildLeafId);
            }
        }

        public void addChild(AbstractLeaf newChildLeaf){
            addChild(newChildLeaf.getId());
        }

        public void clearChildren(){
            childLeafIds = new ArrayList<>();
        }

        // endregion

        // region to JObject methods

        @Override
        public JObject toJObject() {

            JObject output = JObject.createEmptyDictionary();
            output.setTypeName(getClass().getSimpleName());
            output.setValue("id", id);
            output.setValue("parentLeafId", parentLeafId);
            output.setValue("childLeafIds", childLeafIds);
            output.setValue("payloadStrings", payloadStrings);

            return output;
        }

        // endregion

        // region toString() functions

        public String toDebugString(){
            return toDebugString(0);
        }

        public String toDebugString(int startingIndentation) {
            return toJObject().toIndentedString(startingIndentation);
        }

        public String toString(){
            return toDebugString();
        }

        // endregion
    }

    protected boolean treeBuilt;
    protected JString errorDescription;
    protected ArrayList<AbstractLeaf> leafs;

    // region tree built

    public boolean isTreeBuilt(){
        return treeBuilt;
    }

    protected void setTreeBuilt(boolean newValue){
        treeBuilt = newValue;
    }

    // endregion

    // region error description

    protected void setErrorDescription(JString newValue){
        errorDescription = newValue.clone();
    }

    public String getErrorDescription(){
        return errorDescription.toString();
    }

    // endregion

    // region leafs

    int getIndexOfLeaf(long id){
        int counter = 0;
        for(AbstractLeaf leaf : leafs){
            if(leaf.getId() == id){
                return  counter;
            }
            else{
                counter += 1;
            }
        }
        return -1;
    }

    int getIndexOfLeaf(CharSequence id){
        JString idString = new JString(id);
        if(idString.startsWith(AbstractLeaf.getIdOpeningBracket()) && idString.endsWith(AbstractLeaf.getIdClosingBracket())) {
            JPrimitive ps = new JPrimitive(idString.removeEndCapBrackets(AbstractLeaf.getIdOpeningBracket(), AbstractLeaf.getIdClosingBracket()));
            if (ps.isNumber()) {
                return getIndexOfLeaf(ps.jNumberValue().longValue());
            }
        }
        return -1;
    }

    public boolean isAcceptableIndex(int index){
        return index >= 0 && index < leafs.size();
    }

    public AbstractLeaf getLeaf(long index){
        for(AbstractLeaf temp : this.leafs){
            if(temp.getId() == index){
                return temp;
            }
        }
        return null;
    }

    public AbstractLeaf getLeaf(CharSequence id){
        int index = getIndexOfLeaf(id);
        return getLeaf(index);
    }

    public AbstractLeaf getRootLeaf(){
        if (isTreeBuilt()){
            if(getLeafCount() > 0) {
                return leafs.get(0);
            }
        }
        return null;
    }

    public int getLeafCount(){
        return leafs.size();
    }

    public void removeLeaf(AbstractLeaf target){
        leafs.remove(target);
    }

    public AbstractLeaf[] getTopLeafs(){
        ArrayList<AbstractLeaf> output = new ArrayList<>();

        for (AbstractLeaf leaf : leafs){
            if (leaf.childLeafIds.size() == 0){
                output.add(leaf);
            }
        }

        return output.toArray(new AbstractLeaf[0]);
    }

    public AbstractLeaf[] getAllLeafs() {
        return leafs.toArray(new AbstractLeaf[0]);
    }

    public AbstractLeaf[] getAllChildren(AbstractLeaf parent){
        ArrayList<AbstractLeaf> output = new ArrayList<>();

        for(long childId : parent.getChildLeafIds()){
            AbstractLeaf childLeaf = getLeaf(childId);
            if(childLeaf != null){
                output.add(childLeaf);
            }
        }

        return output.toArray(new AbstractLeaf[0]);
    }

    // endregion

    // region construction

    protected void checkParent_ChildRelationships(){
        for(AbstractLeaf leaf : leafs){

            // removes ids from the leaf's child leaf list if they do not appear in the leaf's payload string
            for (int x = 0; x < leaf.childLeafIds.size(); x += 1){
                JPrimitive childLeafId = new JPrimitive(AbstractLeaf.constructIdString(leaf.childLeafIds.get(x)));
                boolean found = false;
                for(int y = 0; y < leaf.payloadStrings.length && !found; y += 1){
                    if(leaf.payloadStrings[y].equals(childLeafId)){
                        found = true;
                    }
                }
                if(!found){
                    leaf.removeChild(leaf.childLeafIds.get(x));
                    x -= 1;
                }
            }

            // adds missing children
            JString[] childIdsInPayload = new JString[0];
            if(leaf.payloadStrings != null && leaf.payloadStrings.length > 0) {

                childIdsInPayload = JString.extractSubstringsViaBrackets(
                        JString.concatenateStrings(JString.toJStringArray(leaf.payloadStrings), null, null, null),
                        AbstractLeaf.getIdOpeningBracket(), AbstractLeaf.getIdClosingBracket(), false);
            }

            if(childIdsInPayload.length > 0) {

                // ensures that the order of leaf id in the child list match their order in the payload
                leaf.clearChildren();

                for (JString childId : childIdsInPayload) {
                    leaf.addChild(new JPrimitive(childId).jNumberValue().longValue());
                    AbstractLeaf childLeaf = getLeaf(childId.toString());

                    if (childLeaf != null) {

                        leaf.addChild(childLeaf);

                        childLeaf.parentLeafId = leaf.id;
                    }
                }
            }
        }

    }

    // endregion

    // region convert to JObject

    @Override
    public JObject toJObject() {

        JObject output = JObject.createEmptyDictionary();

        output.setTypeName(getClass().getSimpleName());
        output.setValue("treeBuilt", treeBuilt);
        output.setValue("errorDescription", errorDescription);
        output.setValue("leafs", leafs);

        return output;
    }

    // endregion
}
