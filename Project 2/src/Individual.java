public class Individual {

    private String rule;
    private double selection;
    private double fitness;

    public Individual(String S) {

        rule = S;
        selection = 0;
        fitness = 0;

    }

    public double getFitness() {
        return fitness;
    }

    public double getSelection() {
        return selection;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public void setSelection(double selection) {
        this.selection = selection;
    }
}
