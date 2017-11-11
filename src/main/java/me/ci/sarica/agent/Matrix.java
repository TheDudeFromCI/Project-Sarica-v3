package me.ci.sarica.agent;

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

    public float[] getValues()
    {
        return values;
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

    public float meanError()
    {
        double f = 0f;

        for (int i = 0; i < values.length; i++)
            f += Math.abs(values[i]);

        return (float)(f / values.length);
    }

    public Matrix add(Matrix other)
    {
        if (getRows() != other.getRows() || getCols() != other.getCols())
            throw new IllegalArgumentException("Matrix sizes do not match!");

        Matrix c = new Matrix(getRows(), getCols());

        for (int row = 0; row < c.getRows(); row++)
            for (int col = 0; col < c.getCols(); col++)
                c.setValue(row, col, getValue(row, col) + other.getValue(row, col));

        return c;
    }

    public Matrix transpose()
    {
        Matrix c = new Matrix(getCols(), getRows());

        for (int row = 0; row < c.getRows(); row++)
            for (int col = 0; col < c.getCols(); col++)
                c.setValue(row, col, getValue(col, row));

        return c;
    }

    public Matrix dot(Matrix other)
    {
        if (getCols() != other.getRows())
            throw new IllegalArgumentException("Matrix sizes do not share side dimension!");

        Matrix c = new Matrix(getRows(), other.getCols());

        for (int row = 0; row < c.getRows(); row++)
            for (int col = 0; col < c.getCols(); col++)
            {
                float v = 0f;
                for (int j = 0; j < getCols(); j++)
                    v += getValue(row, j) * other.getValue(j, col);
                c.setValue(row, col, v);
            }

        return c;
    }

    public Matrix sub(Matrix other)
    {
        if (getRows() != other.getRows() || getCols() != other.getCols())
            throw new IllegalArgumentException("Matrix sizes do not match!");

        Matrix c = new Matrix(getRows(), getCols());

        for (int row = 0; row < c.getRows(); row++)
            for (int col = 0; col < c.getCols(); col++)
                c.setValue(row, col, getValue(row, col) - other.getValue(row, col));

        return c;
    }

    public Matrix mul(Matrix other)
    {
        if (getRows() != other.getRows() || getCols() != other.getCols())
            throw new IllegalArgumentException("Matrix sizes do not match!");

        Matrix c = new Matrix(getRows(), getCols());

        for (int row = 0; row < c.getRows(); row++)
            for (int col = 0; col < c.getCols(); col++)
                c.setValue(row, col, getValue(row, col) * other.getValue(row, col));

        return c;
    }

    public Matrix sigmoid()
    {
        Matrix c = new Matrix(getRows(), getCols());

        for (int i = 0; i < values.length; i++)
            c.setValueByIndex(i, sigmoid(values[i]));

        return c;
    }

    public Matrix round()
    {
        Matrix c = new Matrix(getRows(), getCols());

        for (int i = 0; i < values.length; i++)
            c.setValueByIndex(i, Math.round(values[i]));

        return c;
    }

    public Matrix mul(float s)
    {
        Matrix c = new Matrix(getRows(), getCols());

        for (int i = 0; i < values.length; i++)
            c.setValueByIndex(i, values[i] * s);

        return c;
    }

    private float sigmoid(float x)
    {
        x = Math.min(15, Math.max(-15, x));
        return 1f / (1f + (float)Math.exp(-x));
    }

    private float sigmoidDeriv(float x)
    {
        return x * (1f - x);
    }

    public Matrix sigmoidDeriv()
    {
        Matrix c = new Matrix(getRows(), getCols());

        for (int i = 0; i < values.length; i++)
            c.setValueByIndex(i, sigmoidDeriv(values[i]));

        return c;
    }
}
