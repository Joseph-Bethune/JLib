package jLibrary.nestedStringTree;

import jLibrary.JObject;
import jLibrary.JString;
import jLibrary.miscFunctions.MiscFunctions;
import jLibrary.JPrimitive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import static jLibrary.miscFunctions.MiscFunctions.print;

/**
 * This class is used to break a string down into a series of "leafs" contained within a "tree".
 * <br>
 * Each leaf represents a substring that was enclosed within a set of brackets within the root string.
 * @author Joseph Bethune
 */
public class NestedStringTree_Brackets extends AbstractNestedStringTree {

    /**
     * This class is used to break a string down into a series of "leafs" contained within a "tree"
     * <br>
     * Each leaf represents a substring that was enclosed within a set of brackets within the root string
     * @author Joseph Bethune
     */
	public static class Leaf extends AbstractNestedStringTree.AbstractLeaf{
        Boolean hasBrackets;
        String openingBracket, closingBracket;
        Boolean hasCommas;

        // region constructors

        public Leaf(){
            clear();
        }

        public Leaf(Leaf original){
            setTo(original);
        }

        // endregion

        // region basic class functions

        public void clear(){
            super.clear();
            hasBrackets = false;
            hasCommas = false;
            openingBracket = null;
            closingBracket = null;
        }

        public void setTo(Leaf original){
            super.setTo(original);

            hasBrackets = original.hasBrackets;
            openingBracket = original.openingBracket;
            closingBracket = original.closingBracket;
            hasCommas = original.hasCommas;
        }

        // endregion

        // region brackets

        public Boolean hasBrackets() {
            return hasBrackets;
        }

        public String getOpeningBracket(){
            return openingBracket;
        }

        public String getClosingBracket() {
            return closingBracket;
        }

        public void setBrackets(String openingBracket, String closingBracket) {
            this.openingBracket = openingBracket;
            this.closingBracket = closingBracket;

            if(!MiscFunctions.isEmptyString(this.openingBracket) && !MiscFunctions.isEmptyString(this.closingBracket)) {
                this.hasBrackets = true;
            }
            else {
                this.hasBrackets = false;
            }
        }

        // endregion

        // region payload string

        public String buildPayloadString(){

            JString payloadStringOutput = new JString();
            for(JPrimitive ele : payloadStrings){
                payloadStringOutput.append(ele.toJString());
            }

            if(hasBrackets) {
                payloadStringOutput.prepend(openingBracket).append(closingBracket);
            }

            payloadStringOutput.shrinkWrap();
            return payloadStringOutput.toString();
        }

        // endregion

        // region commas

        public Boolean hasCommas() {
        	return hasCommas;
        }

        // endregion

        // region to JObject methods

        @Override
        public JObject toJObject() {

            JObject output = super.toJObject();
            output.setValue("hasBrackets", hasBrackets);
            output.setValue("openingBracket", openingBracket);
            output.setValue("closingBracket", closingBracket);
            output.setValue("hasCommas", hasCommas);

            return output;
        }

        // endregion

        // region to string

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

    JString[] openingBrackets, closingBrackets;

    // region constructors

    public NestedStringTree_Brackets(){
        clear();
    }

    public NestedStringTree_Brackets(JString inputString){
        buildTree(inputString, getDefaultOpeningBrackets(), getDefaultClosingBrackets());
    }

    public NestedStringTree_Brackets(JString inputString, JString[] openingBrackets, JString[] closingBrackets){
        buildTree(inputString, openingBrackets, closingBrackets);
    }

    public NestedStringTree_Brackets(JString inputString, JString openingBrackets, JString closingBrackets){
        buildTree(inputString, new JString[]{openingBrackets}, new JString[]{closingBrackets});
    }

    public NestedStringTree_Brackets(String inputString){
        buildTree(new JString(inputString), getDefaultOpeningBrackets(), getDefaultClosingBrackets());
    }

    public NestedStringTree_Brackets(String inputString, String[] openingBrackets, String[] closingBrackets){
        buildTree(inputString, openingBrackets, closingBrackets);
    }

    public NestedStringTree_Brackets(String inputString, String openingBrackets, String closingBrackets){
        buildTree(inputString, new String[]{openingBrackets}, new String[]{closingBrackets});
    }

    // endregion

    // region basic class functions

    public void clear(){
        if (leafs != null){
            leafs.clear();
        }
        else{
            leafs = new ArrayList<>();
        }

        openingBrackets = new JString[0];
        closingBrackets = new JString[0];

        treeBuilt = false;
        errorDescription = new JString("No construction executed yet.");
    }

    // endregion

    // region tree construction

    private boolean buildTree_core(JString inputString, JString[] inputOpeningBrackets, JString[] inputClosingBrackets){

        clear();

        // early exit for empty strings
        if(inputString == null || inputString.length() < 1){
            errorDescription = new JString("Empty string");
            return true;
        }
        inputString = inputString.clone().trim().shrinkWrap();

        // ensures that there are an equal number of opening and closing brackets
        if (openingBrackets.length != closingBrackets.length) {
            errorDescription = new JString("The opening bracket list must be the same size as the closing bracket list.");
            throw new RuntimeException(errorDescription.toString());
        }

        // ensures that there are no empty bracket strings
        for(JString x : inputOpeningBrackets){
            if (JString.isNullOrEmptyString(x.clone().trim())) {
                errorDescription = new JString("One of the opening brackets is an empty string : "+x);
                throw new RuntimeException(errorDescription.toString());
            }
        }

        for(JString x : inputClosingBrackets){
            if (JString.isNullOrEmptyString(x.clone().trim())) {
                errorDescription = new JString("One of the closing brackets is an empty string : "+x);
                throw new RuntimeException(errorDescription.toString());
            }
        }

        openingBrackets = inputOpeningBrackets;
        closingBrackets = inputClosingBrackets;

        boolean continueLoop = true;
        boolean firstRun = true;
        Leaf rootLeaf = new Leaf();
        rootLeaf.setId(0);
        rootLeaf.payloadStrings = new JPrimitive[]{ new JPrimitive(inputString)};

        Vector<Leaf> que = new Vector<>();
        que.add(rootLeaf);
        int idCounter = 1;

        while(continueLoop){
            Leaf loopLeaf = null;
            if(que.size() > 0){
                loopLeaf = que.get(0);
                que.remove(0);
            }

            if (loopLeaf == null){
                continueLoop = false;
                continue;
            }

            JString[] workingStrings = null;

            if(firstRun) {
                workingStrings = JString.splitStringForExpressionProcessing(loopLeaf.payloadStrings[0].toJString());
            }
            else{
                JString[] temp2 = new JString[loopLeaf.getPayloadSubstrings().length];
                for(int x= 0; x < temp2.length; x+= 1){
                    temp2[x] = loopLeaf.getPayloadSubstrings()[x].toJString();
                }
                workingStrings = temp2;
            }
            loopLeaf.payloadStrings = null;

            // region removes encapsulating brackets from current working string

            if(!loopLeaf.hasBrackets) {
                if(workingStrings.length == 1){
                    JString targetOpeningBracket = null;
                    JString targetClosingBracket = null;
                    boolean conditionMet = false;
                    for (int x = 0; x < openingBrackets.length && !conditionMet; x++) {
                        targetOpeningBracket = openingBrackets[x];
                        targetClosingBracket = closingBrackets[x];
                        if(!workingStrings[0].startsWith(targetOpeningBracket)){
                            continue;
                        }
                        if(!workingStrings[0].endsWith(targetClosingBracket)){
                            continue;
                        }
                        conditionMet = JString.doMatchedBracketsEncapsulateEntireString(
                                workingStrings[0], targetOpeningBracket, targetClosingBracket);
                        if(conditionMet){
                            loopLeaf.hasBrackets = true;
                            loopLeaf.openingBracket = targetOpeningBracket.toString();
                            loopLeaf.closingBracket = targetClosingBracket.toString();
                            workingStrings[0].removeEndCapBrackets(targetOpeningBracket, targetClosingBracket);
                            loopLeaf.payloadStrings = new JPrimitive[]{new JPrimitive(workingStrings[0])};
                        }
                    }
                }
                else if(workingStrings.length > 0){
                    boolean conditionMet = false; // string is encapsulated by brackets

                    JString targetOpeningBracket = null;
                    JString targetClosingBracket = null;
                    for (int x = 0; x < openingBrackets.length && !conditionMet; x++) {
                        targetOpeningBracket = openingBrackets[x];
                        targetClosingBracket = closingBrackets[x];

                        if(workingStrings[0].equals(targetOpeningBracket) &&
                                workingStrings[workingStrings.length-1].equals(targetClosingBracket)){

                            // working string array starts with the target bracket and ends with the
                            // corresponding target bracket

                            int endingIndex = MiscFunctions.getIndexOfMatchingBracket(
                                    workingStrings, targetOpeningBracket, targetClosingBracket, 0);

                            if(endingIndex == workingStrings.length-1){
                                conditionMet = true;

                                ArrayList<JString> temp = new ArrayList<>();
                                loopLeaf.hasBrackets = true;
                                loopLeaf.openingBracket = targetOpeningBracket.toString();
                                loopLeaf.closingBracket = targetClosingBracket.toString();
                                for(int y = 1; y < endingIndex; y += 1){
                                    temp.add(workingStrings[y]);
                                }
                                workingStrings = temp.toArray(new JString[0]);
                            }
                        }
                    }
                }
            }
            // endregion

            // region finds and extracts any additional bracketed substrings in the working string
            boolean workingStringHasBracketSets = true;
            while (workingStringHasBracketSets) {
                // region find the first instance of an element from the opening brackets list inside the working string
                boolean continueBracketSearchLoop = true;
                int openingBracketIndex = -1;
                int closingBracketIndex = -1;
                JString targetOpeningBracket = new JString();
                JString targetClosingBracket = new JString();
                int bracketSearchIndex = 0;
                while(continueBracketSearchLoop){
                    if(bracketSearchIndex >= openingBrackets.length){
                        continueBracketSearchLoop = false;
                        continue;
                    }
                    JString searchedOpeningBracket = openingBrackets[bracketSearchIndex];
                    JString searchedClosingBracket = closingBrackets[bracketSearchIndex];
                    int openingFoundAt = MiscFunctions.indexOf(workingStrings, searchedOpeningBracket);
                    if (openingFoundAt != -1) {
                        int closingFoundAt = MiscFunctions.getIndexOfMatchingBracket(
                                workingStrings, searchedOpeningBracket, searchedClosingBracket, openingFoundAt);

                        if(closingFoundAt != -1){
                            openingBracketIndex = openingFoundAt;
                            closingBracketIndex = closingFoundAt;
                            targetOpeningBracket = searchedOpeningBracket;
                            targetClosingBracket = searchedClosingBracket;
                            continueBracketSearchLoop = false;
                            continue;
                        }
                        else{
                            if(!searchedOpeningBracket.equals("'")){
                                clear();
                                errorDescription = new JString("The opening bracket \"");
                                errorDescription.append(
                                        searchedOpeningBracket).append("\" is missing a matching closing bracket \"").append(
                                        searchedClosingBracket).append("\" in working string \"").append(
                                                JString.concatenateStrings(
                                                        workingStrings, null, null, null))
                                        .append("\"")
                                        .append(" in the root string \"").append(inputString).append(
                                        "\".");
                                throw new RuntimeException(errorDescription.toString());
                            }
                        }
                    }
                    bracketSearchIndex += 1;
                }

                if(openingBracketIndex < 0 || closingBracketIndex < 0){
                    workingStringHasBracketSets = false;
                    continue;
                }

                // endregion

                // region extracts the substring and puts it into the processing que
                ArrayList<JString> newWorkingStrings = new ArrayList<>();
                ArrayList<JPrimitive> leafStrings = new ArrayList<>();
                boolean alreadyAddedReplacementString = false;
                JString replacementString = new JString(Leaf.getIdOpeningBracket()).clone().append(
                        idCounter).append(Leaf.getIdClosingBracket());
                for(int x= 0; x < workingStrings.length; x += 1){
                    if(x == openingBracketIndex || x == closingBracketIndex){
                        // do nothing
                    }
                    else if(x > openingBracketIndex &&  x < closingBracketIndex){
                        leafStrings.add(new JPrimitive(workingStrings[x]));
                        if(!alreadyAddedReplacementString){
                            newWorkingStrings.add(replacementString);
                            alreadyAddedReplacementString = true;
                        }
                    }
                    else{
                        newWorkingStrings.add(workingStrings[x]);
                    }
                }

                Leaf newLeaf = new Leaf();
                newLeaf.id = idCounter;
                newLeaf.payloadStrings = leafStrings.toArray(new JPrimitive[0]);
                newLeaf.hasBrackets = true;
                newLeaf.openingBracket = targetOpeningBracket.toString();
                newLeaf.closingBracket = targetClosingBracket.toString();
                newLeaf.parentLeafId = loopLeaf.id;
                loopLeaf.addChild(newLeaf);
                idCounter += 1;

                que.add(newLeaf);

                workingStrings = newWorkingStrings.toArray(new JString[0]);
            }
            // endregion

            // region extracts comma separated elements in the working string
            for(int x = 0; x < workingStrings.length && !loopLeaf.hasCommas; x += 1){
                if(workingStrings[x].equals(",")){
                    loopLeaf.hasCommas = true;
                }
            }

            ArrayList<JPrimitive> temp = new ArrayList<>();
            for(JString ele : workingStrings){
                temp.add(new JPrimitive(ele));
            }

            loopLeaf.payloadStrings = temp.toArray(new JPrimitive[0]);
            leafs.add(loopLeaf);

            if (firstRun) {
                firstRun = false;
            }

            // endregion
        }

        // region corrects errors in each leaf's child and parent leaf parameters
        checkParent_ChildRelationships();
        // endregion
        //*/

        treeBuilt = true;
        errorDescription = null;

        return true;
    }

    public boolean buildTree(CharSequence inputString, CharSequence[] inputOpeningBrackets, CharSequence[] inputClosingBrackets){
        return buildTree_core(new JString(inputString), JString.toJStringArray(inputOpeningBrackets), JString.toJStringArray(inputClosingBrackets));
    }

    public boolean buildTree(CharSequence inputString){
        return buildTree(new JString(inputString), getDefaultOpeningBrackets(), getDefaultClosingBrackets());
    }

    public JString getConstructionErrorDescription(){
        return errorDescription;
    }

    public boolean isTreeBuilt(){
        return treeBuilt;
    }

    // endregion

    // region collapsing single element objects

    public void collapseSingleElementObjects(){
        ArrayList<Leaf> que = new ArrayList<>();
        que.add(getRootLeaf());
        boolean continueLoop = true;

        while (continueLoop){
            Leaf loopLeaf = que.size() > 0 ? que.get(0) : null;
            if(loopLeaf == null){
                continueLoop = false;
                continue;
            }
            else{
                que.remove(0);
            }

            Leaf[] loopChildren = getAllChildren(loopLeaf);
            boolean newChildrenAdded = false;
            for(Leaf child : loopChildren){
                if(child.getPayloadSubstrings().length == 1 &&
                        child.hasBrackets() &&
                        child.getOpeningBracket().equals("(") &&
                        child.getClosingBracket().equals(")")
                ){
                    JPrimitive ele = child.getPayloadSubstrings()[0];
                    JString stringToReplace = new JString(child.getIdString());

                    loopLeaf.removeChild(child);
                    loopLeaf.replacePayloadSubstring(stringToReplace.toString(), ele.toString());
                    removeLeaf(child);
                    Leaf newChildLeaf = getLeaf(ele.toString());
                    if(newChildLeaf != null){
                        loopLeaf.addChild(newChildLeaf);
                        newChildLeaf.parentLeafId = loopLeaf.getId();
                        newChildrenAdded = true;
                    }
                }
            }

            if(!loopLeaf.hasBrackets || !loopLeaf.getOpeningBracket().equals("[")) {
                ArrayList<JPrimitive> list = new ArrayList<>();
                for (int x = 0; x < loopLeaf.payloadStrings.length; x += 1) {
                    JPrimitive ele = loopLeaf.payloadStrings[x];
                    JPrimitive pele = x > 0 ? loopLeaf.payloadStrings[x - 1] : null;

                    if(ele.isNumber()) {
                        if (pele != null) {
                            if (pele.equals(")") || pele.isNumber()) {
                                list.add(new JPrimitive("*"));
                            }
                        }
                    }

                    list.add(ele);
                }
                loopLeaf.payloadStrings = list.toArray(new JPrimitive[0]);
            }

            loopChildren = getAllChildren(loopLeaf);
            que.addAll(Arrays.asList(loopChildren));
            if(newChildrenAdded){
                que.add(loopLeaf);
            }
        }
    }

    // endregion

    // region rebuilding original string

    public JString rebuildString(Leaf leaf){
        if (leaf == null) {
            return null;
        }

        if (!isTreeBuilt()) {
            return null;
        }

        JString output = new JString();
        ArrayList<Leaf> que = new ArrayList<>();
        que.add(leaf);
        boolean firstRun = true;
        boolean continueLoop = true;

        while(continueLoop){

            Leaf loopLeaf = null;

            if (que.size() > 0)
            {
                loopLeaf = que.get(0);
                que.remove(0);
            }

            if(loopLeaf == null)
            {
                continueLoop = false;
                continue;
            }

            if(firstRun) {
                output = new JString(loopLeaf.buildPayloadString());
            }

            if(loopLeaf.childLeafIds.size() > 0){
                for(long childId : loopLeaf.childLeafIds){
                    Leaf childLeaf = getLeaf(childId);
                    if(childLeaf != null){
                        output = output.replaceEach(childLeaf.getIdString(), childLeaf.buildPayloadString());
                        que.add(childLeaf);
                    }
                }
            }

            if(firstRun) {
                firstRun = false;
            }
        }

        return output;
    }

    public JString rebuildString(){
        if (!isTreeBuilt()) {
            return null;
        }

        Leaf rootLeaf = getRootLeaf();

        return rebuildString(rootLeaf);
    }

    public JPrimitive[] rebuildStringAsArray(Leaf targetLeaf){
        if (targetLeaf == null) {
            return null;
        }

        if (!isTreeBuilt()) {
            return null;
        }

        ArrayList<JPrimitive> set1 = new ArrayList<>();
        if(targetLeaf.hasBrackets){
            set1.add(new JPrimitive(targetLeaf.getOpeningBracket()));
        }
        for(JPrimitive ele : targetLeaf.getPayloadSubstrings()){
            Leaf subLeaf = getLeaf(ele.toJString().toString());
            if(subLeaf == null){
                set1.add(ele);
            }
            else{
                JPrimitive[] subLeafStrings = rebuildStringAsArray(subLeaf);
                for(JPrimitive subEle : subLeafStrings){
                    set1.add(subEle);
                }
            }
        }

        if(targetLeaf.hasBrackets){
            set1.add(new JPrimitive(targetLeaf.getClosingBracket()));
        }

        return set1.toArray(new JPrimitive[0]);
    }

    public JPrimitive[] rebuildStringAsArray(){
        if (!isTreeBuilt()) {
            return null;
        }

        Leaf rootLeaf = getRootLeaf();

        return rebuildStringAsArray(rootLeaf);
    }

    // endregion

    // region default brackets

    public static JString[] getDefaultOpeningBrackets(){
        return JString.toJStringArray(MiscFunctions.getOpeningBrackets());
    }

    public static JString[] getDefaultClosingBrackets(){
        return JString.toJStringArray(MiscFunctions.getClosingBrackets());
    }

    // endregion

    // region list functions

    public Leaf getLeaf(long index){
        AbstractLeaf temp = super.getLeaf(index);
        if(temp != null){
            return (Leaf)temp;
        }
        return null;
    }

    public Leaf getLeaf(CharSequence id){
        int index = getIndexOfLeaf(id);
        return getLeaf(index);
    }

    public Leaf getRootLeaf(){
        try {
            return (Leaf) super.getRootLeaf();
        }
        catch (NullPointerException e){
            return null;
        }
        catch (Exception e){
            throw e;
        }
    }

    public Leaf[] getTopLeafs(){
        return Arrays.stream(super.getTopLeafs()).map(abstractLeaf -> {return(Leaf)abstractLeaf;}).toArray(Leaf[]::new);
    }
    
    public Leaf[] getAllLeafs() {
        return Arrays.stream(super.getAllLeafs()).map(abstractLeaf -> {return(Leaf)abstractLeaf;}).toArray(Leaf[]::new);
    }

    public Leaf[] getAllChildren(Leaf parent){

        return parent.getChildLeafIds().stream().map(childId -> {
            return getLeaf(childId);
        }).toArray(Leaf[]::new);
    }

    // endregion

    // region misc variable access

    public JString[] getOpeningBrackets(){
        return openingBrackets;
    }

    public JString[] getClosingBrackets(){
        return closingBrackets;
    }

    // endregion

    // region convert to JObject

    @Override
    public JObject toJObject() {

        JObject output = super.toJObject();

        output.setValue("openingBrackets", openingBrackets);
        output.setValue("closingBrackets", closingBrackets);
        output.setValue("originalString", JObject.createStringJObject(rebuildString()));

        return output;
    }

    // endregion

    // region to debug string

    public String toDebugString() {

        return toJObject().toIndentedString();
    }

    // endregion
}
