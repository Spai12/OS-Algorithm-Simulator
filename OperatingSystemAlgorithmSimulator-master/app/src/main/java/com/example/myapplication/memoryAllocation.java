package com.example.myapplication;
import androidx.appcompat.app.AppCompatActivity;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Arrays;
class ProcessA
{
    String name;
    int pro_size;
    ProcessA next;
    public ProcessA() {
        this.name = null;
        this.pro_size= -1;
        this.next=null;
    }
    public String getName()
    {
        return this.name;
    }
    public int getPSize(){
        return this.pro_size;
    }
    public void setName(String str)
    {
        this.name=str;
    }
    public void setProSize(int pro_size){
        this.pro_size = pro_size;
    }
    public boolean checkFill()
    {
        return this.name != null && this.pro_size >= 0;
    }
}
class MemoryBlock{
    int block_size;
    int assigned_proc;
    MemoryBlock next;
    public MemoryBlock() {
        this.block_size= -1;
        this.assigned_proc=1000;
        this.next=null;
    }
    public int getBSize(){
        return this.block_size;
    }
    public void setBlocksize(int block_size){
        this.block_size=block_size;
    }
    public boolean checkFill()
    {
        return this.block_size >= 0;
    }
    public void setAssigned_proc(int i){
        this.assigned_proc=i;
    }
    public int getAssigned_proc(){
        return this.assigned_proc;
    }
}
public class memoryAllocation extends AppCompatActivity{
    int procNum=0;
    int algorithm = 0;
    int resNum=0;
    ProcessA prohead;
    MemoryBlock memhead;
    LinearLayout baseLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mem_alloc);
        prohead=null;
        memhead=null;
        Button createProcessBt =  findViewById(R.id.create_process);
        Button createResourceBt = findViewById(R.id.create_block);
        Button submitbtn = findViewById(R.id.submit);
        RadioGroup radioGroup = findViewById(R.id.selectAlgorithm);
        createProcessBt.setOnClickListener(view -> {
            ProcessA tail=getProTail();
            if(tail==null) {
                procNum = procNum + 1;
                addProcessInput(procNum);
            }
            else if(tail.checkFill()) {
                procNum = procNum + 1;
                addProcessInput(procNum);
            }
            else {
                Toast.makeText(memoryAllocation.this, "You did not enter values", Toast.LENGTH_SHORT).show();
            }
        });
        createResourceBt.setOnClickListener(view -> {
            MemoryBlock tail=getBlockTail();
            if(tail==null) {
                resNum = resNum + 1;
                addResourceInput(resNum);
            }
            else if(tail.checkFill()) {
                resNum = resNum + 1;
                addResourceInput(resNum);
            }
            else
                Toast.makeText(memoryAllocation.this, "You did not enter values", Toast.LENGTH_SHORT).show();
        });
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if(checkedId == R.id.firstfitbtn) {
                algorithm = 1;
            } else if(checkedId == R.id.bestfitbtn) {
                algorithm = 2;
            } else if(checkedId == R.id.worstfitbtn) {
                algorithm = 3;
            }

        });
        submitbtn.setOnClickListener(view -> {
            ProcessA proc=getProTail();
            MemoryBlock mem=getBlockTail();
            if(proc.checkFill() && mem.checkFill() && algorithm!=0) {
                int[] ans=takeSubmission(algorithm, procNum, resNum);
                boolean canAlloc=true;
                for (int an : ans) {
                    if (an == -1) {
                        canAlloc = false;
                        break;
                    }
                }
                if(canAlloc) {
                    baseLayout = findViewById(R.id.mainLayout);
                    baseLayout.removeAllViews();
                    animateMemAlloc();
                }
                else
                    Toast.makeText(memoryAllocation.this,"Cannot allocate memory",Toast.LENGTH_LONG).show();
            }
            else
                Toast.makeText(memoryAllocation.this, "You did not enter values", Toast.LENGTH_SHORT).show();
        });
    }
    private void addProcessInput(int procNum) {
        LinearLayout layout= findViewById(R.id.processCreateCol);

        TextInputLayout newInput= new TextInputLayout(this);

        newInput.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        EditText editText= new EditText(this);
        editText.setLayoutParams(editTextParams);
        editText.setHint("Process " + procNum + "size");
        editText.setInputType(EditorInfo.TYPE_CLASS_NUMBER);

        newInput.addView(editText);
        layout.addView(newInput);


        ProcessA newProc;
        if(prohead==null)
        {
            prohead=new ProcessA();
            newProc=prohead;
        }
        else
        {
            ProcessA temp=getProTail();
            newProc=new ProcessA();
            temp.next=newProc;
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            public void afterTextChanged(Editable s) {
                String input = editText.getText().toString();
                boolean filled;
                filled = !input.isEmpty();
                if (filled){
                    newProc.setName("P" + procNum);
                    newProc.setProSize(Integer.parseInt(input));
                }
            }
        });
    }
    private void addResourceInput(int resNum) {

        LinearLayout layout = findViewById(R.id.blockCreateCol);
        TextInputLayout newInput = new TextInputLayout(this);
        newInput.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        EditText editText = new EditText(this);
        editText.setLayoutParams(editTextParams);
        editText.setHint("Block " + resNum + "size");
        editText.setInputType(EditorInfo.TYPE_CLASS_NUMBER);

        layout.addView(newInput);
        newInput.addView(editText, editTextParams);

        MemoryBlock newBlock;
        if(memhead==null) {
            memhead=new MemoryBlock();
            newBlock=memhead;
        }
        else {
            MemoryBlock temp=getBlockTail();
            newBlock=new MemoryBlock();
            temp.next=newBlock;
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            public void afterTextChanged(Editable s) {
                String input = editText.getText().toString();
                boolean filled;
                filled = !input.isEmpty();
                if (filled)
                    newBlock.setBlocksize(Integer.parseInt(input));
            }
        });
    }
    private ProcessA getProTail() {
        ProcessA result = null;
        if (prohead != null) {
            ProcessA temp = prohead;
            while (temp.next != null) {
                temp = temp.next;
            }
            result = temp;
        }
        return result;
    }
    private MemoryBlock getBlockTail() {
        MemoryBlock result = null;
        if (memhead != null) {
            MemoryBlock temp = memhead;
            while (temp.next != null) {
                temp = temp.next;
            }
            result = temp;
        }
        return result;
    }
    private String getProcName(int num) {
        ProcessA temp=prohead;
        int count=0;
        while(count<num){
            temp=temp.next;
            count+=1;
        }
        return temp.getName();
    }
    private int getProSize(int num) {
        ProcessA temp=prohead;
        int count=0;
        while(count<num) {
            temp=temp.next;
            count+=1;
        }
        return temp.getPSize();
    }
    private int getBlockSize(int num) {
        MemoryBlock temp=memhead;
        int count=0;
        while(count<num) {
            temp=temp.next;
            count+=1;
        }
        return temp.getBSize();
    }
    private void setBlockAssignedProc(int num,int proc){
        MemoryBlock temp=memhead;
        int count=0;
        while(count<num) {
            temp=temp.next;
            count+=1;
        }
        temp.setAssigned_proc(proc);
    }
    private int getblockAssignedProc(int num){
        MemoryBlock temp=memhead;
        int count=0;
        while(count<num) {
            temp=temp.next;
            count+=1;
        }
        return temp.getAssigned_proc();
    }
    private int[] takeSubmission(int algorithm,int n,int m){
        int[] allocation = new int[n];
        int[] blockSize = new int[m];
        int[] processSize=new int[n];
        for(int i=0;i<n;i++){
            processSize[i] = getProSize(i);
        }
        for(int i=0;i<m;i++){
            blockSize[i] = getBlockSize(i);
        }
        Arrays.fill(allocation, -1);
        if(algorithm == 1){
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    if (blockSize[j] >= processSize[i]) {
                        allocation[i] = j;
                        setBlockAssignedProc(j,i);
                        blockSize[j] -= processSize[i];
                        break;
                    }
                }
            }
        }
        else if(algorithm==2){
            for (int i=0; i<n; i++) {
                int bestIdx = -1;
                for (int j=0; j<m; j++) {
                    if (blockSize[j] >= processSize[i]) {
                        if (bestIdx == -1)
                            bestIdx = j;
                        else if (blockSize[bestIdx] > blockSize[j])
                            bestIdx = j;
                    }
                }
                if (bestIdx != -1) {
                    allocation[i] = bestIdx;
                    setBlockAssignedProc(bestIdx,i);
                    blockSize[bestIdx] -= processSize[i];
                }
            }
        }
        else{
            for (int i=0; i<n; i++) {
                int wstIdx = -1;
                for (int j=0; j<m; j++) {
                    if (blockSize[j] >= processSize[i]) {
                        if (wstIdx == -1)
                            wstIdx = j;
                        else if (blockSize[wstIdx] < blockSize[j])
                            wstIdx = j;
                    }
                }
                if (wstIdx != -1) {
                    allocation[i] = wstIdx;
                    setBlockAssignedProc(wstIdx,i);
                    blockSize[wstIdx] -= processSize[i];
                }
            }
        }
        System.out.println("\nProcess No.\tProcess Size\tBlock no.");
        for (int i = 0; i < n; i++) {
            System.out.print(" " + (i+1) + "\t\t" +
                    processSize[i] + "\t\t");
            if (allocation[i] != -1)
                System.out.print(allocation[i] + 1);
            else {
                System.out.print("Not Allocated");
                Arrays.fill(allocation, -1);
            }
            System.out.println();
        }
        return allocation;
    }
    private void animateMemAlloc() {
        baseLayout.setOrientation(LinearLayout.VERTICAL);
        for (int i = 0; i < resNum; i++) {
            for (int j = i+1; j < resNum; j++) {
                if(getblockAssignedProc(i)>getblockAssignedProc(j))
                    exchangeBlock(i,j);
            }
        }
        int procSum=0,memSum=0;
        int boxScale=300;
        for (int i = 0; i < procNum; i++) {
            procSum+=getProSize(i);
        }
        for (int i = 0; i < procNum; i++) {
            memSum+=getBlockSize(i);
        }
        int divider= Math.max(procSum,memSum);
        TextView[] procBoxes= new TextView[procNum];
        TextView[] memBoxes=new TextView[procNum];
        for (int i = 0; i < procNum; i++) {
            TextView newText= new TextView(memoryAllocation.this);
            TextView newMem= new TextView(memoryAllocation.this);
            float proportion=Math.max((float)getProSize(i)/divider*boxScale,(float)getBlockSize(i)/divider*boxScale);
            LinearLayout.LayoutParams sliceScreen=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(int)proportion);
            LinearLayout slice=new LinearLayout(memoryAllocation.this);
            slice.setLayoutParams(sliceScreen);
            slice.setOrientation(LinearLayout.HORIZONTAL);
            slice.setGravity(Gravity.START);
            slice.setPadding(0,20,0,0);
            baseLayout.addView(slice);
            proportion=(float)getProSize(i)/divider*boxScale;
            LinearLayout.LayoutParams procTextParams=new LinearLayout.LayoutParams(80,(int)proportion);
            newText.setLayoutParams(procTextParams);
            newText.setBackgroundColor(Color.GREEN);
            newText.setText(getProcName(i));
            newText.setGravity(Gravity.CENTER);
            newText.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
            proportion=(float)getBlockSize(i)/divider*boxScale;
            LinearLayout.LayoutParams memTextParams=new LinearLayout.LayoutParams(90,(int)proportion);
            newMem.setLayoutParams(memTextParams);
            newMem.setBackgroundColor(Color.BLUE);
            String blockName="Size:" + getBlockSize(i);
            newMem.setText(blockName);
            newMem.setTextColor(Color.BLACK);
            newMem.setGravity(Gravity.CENTER);
            newMem.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
            newMem.setPadding(400,0,0,0);
            slice.addView(newText);
            slice.addView(newMem);
            procBoxes[i]=newText;
            memBoxes[i]=newMem;
        }
        long delayStart=400L;
        long delayTimeSlice=1300L;
        for (int i = 0; i < procNum; i++) {
            ObjectAnimator fitHoriz=ObjectAnimator.ofFloat(procBoxes[i],"translationX",80);
            fitHoriz.setDuration(delayTimeSlice);
            Handler h=new Handler();
            int finalI = i;
            h.postDelayed(()->{
                fitHoriz.start();
                memBoxes[finalI].setAlpha(0.5f);
            }, delayStart + i * delayTimeSlice);
        }
    }
    private void exchangeBlock(int b1,int b2) {
        if(b1==b2)
            return;
        MemoryBlock block1=memhead;
        MemoryBlock block2=memhead;
        MemoryBlock prev1=null;
        MemoryBlock prev2=null;
        int count=0;
        while(count<b1) {
            prev1=block1;
            block1=block1.next;
            count+=1;
        }
        count=0;
        while(count<b2) {
            prev2=block2;
            block2=block2.next;
            count+=1;
        }
        if (prev1 != null)
            prev1.next = block2;
        else
            memhead = block2;
        if (prev2 != null)
            prev2.next = block1;
        else
            memhead = block1;
        MemoryBlock temp = block1.next;
        block1.next = block2.next;
        block2.next = temp;
    }
}