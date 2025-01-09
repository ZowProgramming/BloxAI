import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class NeuralNetwork {
    
    NNLayer inputLayer;
    NNLayer hiddenLayer1;
    NNLayer hiddenLayer2;
    NNLayer hiddenLayer3;
    NNLayer outputLayer;

    private int fitness;

    NeuralNetwork(){
        inputLayer = new NNLayer(200);
        hiddenLayer1 = new NNLayer(16,inputLayer);
        hiddenLayer2 = new NNLayer(16,hiddenLayer1);
        hiddenLayer3 = new NNLayer(16,hiddenLayer2);
        outputLayer = new NNLayer(6,hiddenLayer3);
    }

    NeuralNetwork(Board board){
        inputLayer = new NNLayer(200);
        hiddenLayer1 = new NNLayer(16,inputLayer);
        hiddenLayer2 = new NNLayer(16,hiddenLayer1);
        hiddenLayer3 = new NNLayer(16,hiddenLayer2);
        outputLayer = new NNLayer(6,hiddenLayer3);
        inputBoard(board);

    }

    public void setFitness(int val){
        fitness = val;
    }

    public int getFitness(){
        return fitness;
    }

    public void update(){
        hiddenLayer1.calcNeurons();
        hiddenLayer2.calcNeurons();
        hiddenLayer3.calcNeurons();
        outputLayer.calcNeurons();
    }

    public void inputBoard(Board board){
        int count = 0;
        for(int row = 0; row < 20; row++){
            for(int col = 0; col < 10; col++){
                inputLayer.neurons[count].neuronValue = board.blocks[row][col] / 2.0;
                //System.out.println("VALUE:" + inputLayer.neurons[count].neuronValue);
                count++;
            }
        }
    }

    public void mutate(){
        hiddenLayer1.mutate();
        hiddenLayer2.mutate();
        hiddenLayer3.mutate();
        outputLayer.mutate();    
    }

    public int outputNode(){
        double maxVal = 0;
        int output = 0;
        for(int i = 0; i < outputLayer.neurons.length; i ++){
            if(outputLayer.neurons[i].neuronValue > maxVal){
                maxVal = outputLayer.neurons[i].neuronValue;
                output = i;
            }
        }
        return output;
    }

    public void writeToFile(String filename){
        ArrayList<Double> storedNN = new ArrayList<>();

        for(Neuron neuron: hiddenLayer1.neurons){
            for(double weight : neuron.weights){
                storedNN.add(weight);
            }
        }

        for(Neuron neuron: hiddenLayer2.neurons){
            for(double weight : neuron.weights){
                storedNN.add(weight);
            }
        }

        for(Neuron neuron: hiddenLayer3.neurons){
            for(double weight : neuron.weights){
                storedNN.add(weight);
            }
        }

        for(Neuron neuron: outputLayer.neurons){
            for(double weight : neuron.weights){
                storedNN.add(weight);
            }
        }

        try (FileWriter writer = new FileWriter(filename, true)) {
            String newline = System.lineSeparator();
            for(int i = 0; i < storedNN.size(); i++){
                writer.write(storedNN.get(i).toString());

                if (i < storedNN.size() - 1){
                    writer.write(",");
                }
            }

            writer.write(newline);
            
        }
        catch (IOException e){
            e.printStackTrace();
        }


    }

}
