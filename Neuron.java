public class Neuron {
    double neuronValue;
    double weights[]; // weights for values of previous layer. i.e weights[n] is the coefficient neuronValue of neuron n of previous layer
    double bias;

    public void calcNeuronValue(NNLayer layer){
        double sum = 0;

        for(int i = 0; i < layer.neurons.length; i++){
            sum += weights[i]*layer.neurons[i].neuronValue;
        }
        neuronValue = sigmoid(sum - bias);
    }

    public void setNeuronValue(double newVal){
        neuronValue = newVal;
    }

    private double sigmoid(double x){
        return  1 / ( 1 + Math.pow(Math.E, - x) );
    }
    
}
