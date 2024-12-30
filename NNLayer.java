public class NNLayer {

    Neuron neurons[];
    NNLayer previousLayer;

    NNLayer(int size){
        neurons = new Neuron[size];
        previousLayer = null;
    }

    NNLayer(int size, NNLayer previous){
        neurons = new Neuron[size];
        previousLayer = previous;
        generateBiases();
        generateWeights();
    }

    public void calcNeurons(){
        for(Neuron neuron : neurons) {
            neuron.calcNeuronValue(previousLayer);
        }
    }

    private void generateWeights(){
        if(previousLayer == null){
            System.out.println("Cannot Find Previous Layer");
            return;
        }
        for(Neuron neuron : neurons){
            for(int i = 0; i < previousLayer.neurons.length; i++) {
                neuron.weights[i] = 100*Math.random() - 50;
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
        for(Neuron neuron : neurons){
            neuron.bias = 100*Math.random() - 50;
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
