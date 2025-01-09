import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class NeuralNetwork {
    
    NNLayer inputLayer;
    NNLayer hiddenLayer1;
    NNLayer hiddenLayer2;
    NNLayer hiddenLayer3;
    NNLayer outputLayer;

    private int fitness = 0;

    @SuppressWarnings("unused")
    NeuralNetwork(){
        inputLayer = new NNLayer(200);
        hiddenLayer1 = new NNLayer(16,inputLayer);
        hiddenLayer2 = new NNLayer(16,hiddenLayer1);
        hiddenLayer3 = new NNLayer(16,hiddenLayer2);
        outputLayer = new NNLayer(6,hiddenLayer3);
    }

    @SuppressWarnings("OverridableMethodCallInConstructor")
    NeuralNetwork(Board board){
        inputLayer = new NNLayer(200);
        hiddenLayer1 = new NNLayer(16,inputLayer);
        hiddenLayer2 = new NNLayer(16,hiddenLayer1);
        hiddenLayer3 = new NNLayer(16,hiddenLayer2);
        outputLayer = new NNLayer(6,hiddenLayer3);
        
        inputBoard(board);
    }

    @SuppressWarnings("OverridableMethodCallInConstructor")
    NeuralNetwork(Board board, String filename, int line){
        inputLayer = new NNLayer(200);
        hiddenLayer1 = new NNLayer(16,inputLayer);
        hiddenLayer2 = new NNLayer(16,hiddenLayer1);
        hiddenLayer3 = new NNLayer(16,hiddenLayer2);
        outputLayer = new NNLayer(6,hiddenLayer3);
        
        inputBoard(board);
        readFromFile(filename, line);
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

    //method to read specific line written w/ help from ChatGPT
    public void readFromFile(String filename, int lineNum){
        

        ArrayList<Double> storedNNAL = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int currentLine = 0;

            while ((line = br.readLine()) != null) {
                currentLine++;

                // Check if it's the line we want
                if (currentLine == lineNum) {

                    // Split the line into values (assuming comma-separated)
                    String[] stringValues = line.split(",");
                    
                    for (String value : stringValues) {
                        storedNNAL.add(Double.valueOf(value));
                    }
                    // Exit the loop since we've read the desired line
                    break;
                }
            }

            // Handle case where line doesn't exist
            if (currentLine < lineNum) {
                System.out.println("Line " + lineNum + " does not exist in the file.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        double[] storedNN = new double[storedNNAL.size()];
        for(int i = 0; i < storedNNAL.size(); i++){
            storedNN[i] = storedNNAL.get(i);
        }

        for(int i = 0; i < hiddenLayer1.neurons.length; i++){
            hiddenLayer1.neurons[i].weights = Arrays.copyOfRange(storedNN, i*200, (i+1)*200);
        }

        for(int i = 0; i < hiddenLayer2.neurons.length; i++){
            hiddenLayer2.neurons[i].weights = Arrays.copyOfRange(storedNN, 3200 + i*16, 3200 + (i+1)*16);
        }

        for(int i = 0; i < hiddenLayer3.neurons.length; i++){
            hiddenLayer3.neurons[i].weights = Arrays.copyOfRange(storedNN, 3456 + i*16, 3456 + (i+1)*16);
        }

        for(int i = 0; i < outputLayer.neurons.length; i++){
            outputLayer.neurons[i].weights = Arrays.copyOfRange(storedNN, 3712 + i*16, 3712 + (i+1)*16);
        }


    }
}
