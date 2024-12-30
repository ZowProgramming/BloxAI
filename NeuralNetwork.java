public class NeuralNetwork {
    
    NNLayer inputLayer;
    NNLayer hiddenLayer1;
    NNLayer hiddenLayer2;
    NNLayer hiddenLayer3;
    NNLayer outputLayer;

    NeuralNetwork(){
        inputLayer = new NNLayer(200);
        hiddenLayer1 = new NNLayer(16,inputLayer);
        hiddenLayer2 = new NNLayer(16,hiddenLayer1);
        hiddenLayer3 = new NNLayer(16,hiddenLayer2);
        outputLayer = new NNLayer(6,hiddenLayer3);
    }

    public void inputBoard(Board board){
        int count = 0;
        for(int row = 0; row < 20; row++){
            for(int col = 0; col < 10; col++){
                inputLayer.neurons[count].neuronValue = board.blocks[row][col] / 2.0;
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

}
