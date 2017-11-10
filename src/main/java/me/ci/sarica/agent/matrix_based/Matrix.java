package me.ci.sarica.agent.matrix_based;

public class Matrix
{
    private final float[] values;
    private final int rows;
    private final int cols;

    public Matrix(int rows, int cols)
    {
        this.rows = rows;
        this.cols = cols;
        values = new float[rows * cols];
    }

    public int getRows()
    {
        return rows;
    }

    public int getCols()
    {
        return cols;
    }

    public float getValue(int row, int col)
    {
        return values[col * rows + row];
    }

    @Override
    public String toString()
    {
        String s = "";
        s += "[\n";

        for (int r = 0; r < rows; r++)
        {
            s += " [";
            for (int c = 0; c < cols; c++)
            {
                if (c > 0) s += ", ";
                s += getValue(r, c);
            }
            s += "]\n";
        }


        s += "]\n";
        return s;
    }

    public void setValue(int row, int col, float value)
    {
        values[col * rows + row] = value;
    }

    public void setValueByIndex(int index, float value)
    {
        values[index] = value;
    }

    public float getValueByIndex(int index)
    {
        return values[index];
    }

    public int getValueCount()
    {
        return values.length;
    }
}
