public class NNLayer {

    Neuron neurons[];
    NNLayer previousLayer;

    final int WEIGHT_RANGE = 25;
    final int BIAS_RANGE = 25;

    NNLayer(int size){
        neurons = new Neuron[size];
        for(int i = 0; i < size; i++){
            neurons[i] = new Neuron();
        }
        previousLayer = null;
    }

    NNLayer(int size, NNLayer previous){
        neurons = new Neuron[size];
        for(int i = 0; i < size; i++){
            neurons[i] = new Neuron();
        }
        previousLayer = previous;
        generateBiases();
        generateWeights();
        calcNeurons();
    }

    final public void calcNeurons(){
        for(Neuron neuron : neurons) {
            neuron.calcNeuronValue(previousLayer);
        }
    }

    private void generateWeights(){
        if(previousLayer == null){
            System.out.println("Cannot Find Previous Layer");
            return;
        }
        for (Neuron neuron : neurons) {
            neuron.weights = new double[previousLayer.neurons.length];
            for (int i = 0; i < previousLayer.neurons.length; i++) {
                neuron.weights[i] = 2*WEIGHT_RANGE*Math.random() - WEIGHT_RANGE;
            }
        }
    }

    private double randInRange(double a, double b){
        double diff = Math.abs(b-a);
        return a + diff*Math.random();
    }

    private void mutateWeights(){
        for(Neuron neuron : neurons){
            for(int i = 0; i < previousLayer.neurons.length; i++) {
                neuron.weights[i] *= randInRange(0.99,1.01);
            }
        }
    }

    private void generateBiases(){
        for (Neuron neuron : neurons) {
            neuron.bias = BIAS_RANGE*2*Math.random() - BIAS_RANGE;
        }
    }

    private void mutateBiases(){
        for(Neuron neuron : neurons){
            neuron.bias *= randInRange(0.99,1.01);
        }
    }

    public void mutate(){
        mutateBiases();
        mutateWeights();
    }
    

}
