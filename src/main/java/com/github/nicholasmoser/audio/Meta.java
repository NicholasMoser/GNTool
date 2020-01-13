package com.github.nicholasmoser.audio;

import java.util.Arrays;

public class Meta
{
    // See the javadoc for descriptions of each file.
    private int id;
    private int offset;
    private int samples;
    private int nibbles;
    private int rate;
    private int loopFlag;
    private int loopStart;
    private int loopLength;
    private int loopEnd;
    private byte[] coeffs;
    private byte[] ps;
    private byte[] lps;
    private byte[] lyn1;
    private byte[] lyn2;
    
    /**
     * Private constructor to prevent direct instantiation.
     */
    private Meta(int id, int offset, int samples, int nibbles, int rate, int loopFlag, int loopStart, int loopLength, int loopEnd, byte[] coeffs, byte[] ps, byte[] lps, byte[] lyn1, byte[] lyn2)
    {
        this.id = id;
        this.offset = offset;
        this.samples = samples;
        this.nibbles = nibbles;
        this.rate = rate;
        this.loopFlag = loopFlag;
        this.loopStart = loopStart;
        this.loopLength = loopLength;
        this.loopEnd = loopEnd;
        this.coeffs = coeffs;
        this.ps = ps;
        this.lps = lps;
        this.lyn1 = lyn1;
        this.lyn2 = lyn2;
    }

    /**
     * @return The id.
     */
    public int getId()
    {
        return id;
    }

    /**
     * @return The offset.
     */
    public int getOffset()
    {
        return offset;
    }

    /**
     * @return The number of raw samples.
     */
    public int getSamples()
    {
        return samples;
    }

    /**
     * @return The number of nibbles.
     */
    public int getNibbles()
    {
        return nibbles;
    }

    /**
     * @return The sample rate.
     */
    public int getRate()
    {
        return rate;
    }

    /**
     * @return The loop flag.
     */
    public int getLoopFlag()
    {
        return loopFlag;
    }

    /**
     * @return The loop start (in nibbles).
     */
    public int getLoopStart()
    {
        return loopStart;
    }

    /**
     * @return The loop length.
     */
    public int getLoopLength()
    {
        return loopLength;
    }

    /**
     * @return The loop end (in nibbles).
     */
    public int getLoopEnd()
    {
        return loopEnd;
    }

    /**
     * @return The coefficienta.
     */
    public byte[] getCoeffs()
    {
        return coeffs;
    }

    /**
     * @return The predictor/scale
     */
    public byte[] getPs()
    {
        return ps;
    }

    /**
     * @return The predictor/scale for loop context.
     */
    public byte[] getLps()
    {
        return lps;
    }

    /**
     * @return The sample history (n-1) for loop context.
     */
    public byte[] getLyn1()
    {
        return lyn1;
    }

    /**
     * @return The sample history (n-2) for loop context.
     */
    public byte[] getLyn2()
    {
        return lyn2;
    }

    @Override
    public String toString() {
        return "Meta{" +
            "id=" + id +
            ", offset=" + offset +
            ", samples=" + samples +
            ", nibbles=" + nibbles +
            ", rate=" + rate +
            ", loopFlag=" + loopFlag +
            ", loopStart=" + loopStart +
            ", loopLength=" + loopLength +
            ", loopEnd=" + loopEnd +
            ", coeffs=" + Arrays.toString(coeffs) +
            ", ps=" + Arrays.toString(ps) +
            ", lps=" + Arrays.toString(lps) +
            ", lyn1=" + Arrays.toString(lyn1) +
            ", lyn2=" + Arrays.toString(lyn2) +
            '}';
    }

    /**
     * The builder for a Meta object.
     */
    public static class Builder
    {
        private int id;
        private int offset;
        private int samples;
        private int nibbles;
        private int rate;
        private int loopFlag;
        private int loopStart;
        private int loopLength;
        private int loopEnd;
        private byte[] coeffs;
        private byte[] ps;
        private byte[] lps;
        private byte[] lyn1;
        private byte[] lyn2;
        
        public Meta build()
        {
            return new Meta(id, offset, samples, nibbles, rate, loopFlag, loopStart, loopLength, loopEnd, coeffs, ps, lps, lyn1, lyn2);
        }
        
        public Builder withId(int id)
        {
            this.id = id;
            return this;
        }
        
        public Builder withOffset(int offset)
        {
            this.offset = offset;
            return this;
        }
        
        public Builder withSamples(int samples)
        {
            this.samples = samples;
            return this;
        }
        
        public Builder withNibbles(int nibbles)
        {
            this.nibbles = nibbles;
            return this;
        }

        public Builder withRate(int rate)
        {
            this.rate = rate;
            return this;
        }

        public Builder withLoopFlag(int loopFlag)
        {
            this.loopFlag = loopFlag;
            return this;
        }

        public Builder withLoopStart(int loopStart)
        {
            this.loopStart = loopStart;
            return this;
        }

        public Builder withLoopLength(int loopLength)
        {
            this.loopLength = loopLength;
            return this;
        }

        public Builder withLoopEnd(int loopEnd)
        {
            this.loopEnd = loopEnd;
            return this;
        }

        public Builder withCoeffs(byte[] coeffs)
        {
            this.coeffs = coeffs;
            return this;
        }

        public Builder withPs(byte[] ps)
        {
            this.ps = ps;
            return this;
        }

        public Builder withLps(byte[] lps)
        {
            this.lps = lps;
            return this;
        }

        public Builder withLyn1(byte[] lyn1)
        {
            this.lyn1 = lyn1;
            return this;
        }

        public Builder withLyn2(byte[] lyn2)
        {
            this.lyn2 = lyn2;
            return this;
        }
    }
}
